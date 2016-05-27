package com.pandu.patpat.android.staftrack;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
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

    EditText Search;

    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mProfilDAO = new ProfilDAO(getApplicationContext());

        Search = (EditText)findViewById(R.id.search_bar_text);

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

        ProfilDAO mProfilDAO = new ProfilDAO(getApplicationContext());

        Toast.makeText(getApplicationContext(), String.valueOf(mProfilDAO.getProfilCount()), Toast.LENGTH_SHORT).show();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

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
