package com.pandu.patpat.android.staftrack;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    ProfilDAO mProfilDAO;
    LoginDAO mLoginDAO;

    EditText Search;

    public static Activity mapActivity;

    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapActivity = this;

        mProfilDAO = new ProfilDAO(getApplicationContext());
        mLoginDAO = new LoginDAO(getApplicationContext());

        Search = (EditText)findViewById(R.id.search_bar_text);
        final ImageView back = (ImageView) findViewById(R.id.left_action);
        final ImageView cari = (ImageView) findViewById(R.id.cari);
        final ImageView track = (ImageView) findViewById(R.id.track_btn);
        final ImageView seting = (ImageView) findViewById(R.id.setting_btn);

        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    cari.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                } else {
                    cari.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);
                }
            }
        });

        Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(Search.getText().toString().trim().length() > 0)
                    {
                        Intent intent = new Intent(MapActivity.this, SearchResultActivity.class);
                        Bundle search = new Bundle();
                        search.putString("keywords", Search.getText().toString());
                        intent.putExtras(search);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        buildGoogleApiClient();

        final ProfilDAO mProfilDAO = new ProfilDAO(getApplicationContext());

        Toast.makeText(getApplicationContext(), String.valueOf(mProfilDAO.getProfilCount()), Toast.LENGTH_SHORT).show();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cari.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);

                Search.requestFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Search, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Search.clearFocus();

                back.setVisibility(View.GONE);
                cari.setVisibility(View.VISIBLE);
            }
        });



        seting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MapActivity.this, seting);
                popup.getMenuInflater().inflate(R.menu.menu_options, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.profile:
                                Intent intent1 = new Intent(MapActivity.this, ProfileActivity.class);
                                startActivity(intent1);
                                break;

                            case R.id.logout:
                                //Toast.makeText( MapActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                                startActivity(intent);
                                mLoginDAO.updateLoginFromId(1, 0);
                                Profil profil = mProfilDAO.getTopProfil();
                                mProfilDAO.deleteProfil(profil);
                                finish();
                                break;

                            case R.id.about:
                                Intent intent2 = new Intent(MapActivity.this, AboutActivity.class);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });



    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();

    }

    private void saveLokasi(final String jabatan, final String id, final String lat, final String lon) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String versi = params[0];
                String tgl = params[1];

                InputStream is = null;
                String result = null;

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/simpanlokasi.php?jabatan=" + jabatan + "&id=" + id+ "&la=" + lat + "&lon=" + lon);

                        HttpResponse response = httpClient.execute(httpGet);

                        //response = httpClient.execute("patpatstudio.com", "/pakanku/update.php?versi=" + versi + "&tanggal=" + tgl);
                        HttpEntity entity = response.getEntity();
                        String htmlResponse = EntityUtils.toString(entity);
                        result = htmlResponse;
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                return result;


            }

            @Override
            protected void onCancelled() {
                super.onCancelled();

                //loadingDialog.dismiss();
            }

            @Override
            protected void onPostExecute(String result){

            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(jabatan, id, lat, lon);

    }

    @Override
    public void onConnected(Bundle arg0) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            Toast.makeText(getApplicationContext(), "Latitude: "+ String.valueOf(mLastLocation.getLatitude())+"Longitude: "+
                    String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
            LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location"));
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(myLoc);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            saveLokasi(mProfilDAO.getTopProfil().getJabatan_profil(), String.valueOf(mProfilDAO.getTopProfil().getIdprofil()), String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(marker.getTitle().equals("My Location"))
                    {
                        Intent intent = new Intent(MapActivity.this, ContactActivity.class);
                        //Bundle search = new Bundle();
                        //search.putString("keywords", Search.getText().toString());
                        //intent.putExtras(search);
                        startActivity(intent);
                    }
                    return false;
                }
            });

//            t = new Timer();
//            t.schedule(new TimerTask() {
//
//                public void run() {
//
//                    Calendar c = Calendar.getInstance();
//
//                    int hours_calendar = c.get(Calendar.HOUR_OF_DAY);
//                    int minutes_calendar = c.get(Calendar.MINUTE);
//                    int month_calendar = c.get(Calendar.MONTH) + 1;
//                    int date_calendar = c.get(Calendar.DAY_OF_MONTH);
//                    int year_calendar = c.get(Calendar.YEAR);
//
//                    //Toast.makeText(getApplicationContext(), date_calendar + "/" + month_calendar + "/" + year_calendar + "&" + hours_calendar + ":" + minutes_calendar, Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(getBaseContext(), "send", Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                            mGoogleApiClient);
//
//                    if (mLastLocation != null) {
//
//                        try {
//                            HttpClient httpClient = new DefaultHttpClient();
//                            HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/simpanlokasi.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&id=" + mProfilDAO.getTopProfil().getIdprofil() + "&la=" + String.valueOf(mLastLocation.getLatitude()) + "&lo=" + String.valueOf(mLastLocation.getLongitude()));
//
//                            HttpResponse response = httpClient.execute(httpGet);
//
//                            HttpEntity entity = response.getEntity();
//                            String htmlResponse = EntityUtils.toString(entity);
//                        } catch (ClientProtocolException e) {
//                            e.printStackTrace();
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else {
//
//                    }
//                }
//            }, 1000 * 10);

        }

    }

    public void setMarker(String nama, double lat, double longi) {

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                String stredittext=data.getStringExtra("edittextvalue");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }

//    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
//            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
//
//            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
//
//            if(currentNetworkInfo.isConnected()){
//                Toast.makeText(getBaseContext(), "send", Toast.LENGTH_SHORT).show();
//                //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
//
//                if (mLastLocation != null) {
//
//                    try {
//                        HttpClient httpClient = new DefaultHttpClient();
//                        HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/simpanlokasi.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&id=" + mProfilDAO.getTopProfil().getIdprofil() + "&la=" + String.valueOf(mLastLocation.getLatitude()) + "&lo=" + String.valueOf(mLastLocation.getLongitude()));
//
//                        HttpResponse response = httpClient.execute(httpGet);
//
//                        HttpEntity entity = response.getEntity();
//                        String htmlResponse = EntityUtils.toString(entity);
//                    } catch (ClientProtocolException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//
//                }
//
//
//                }else{
//                //Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    };


}
