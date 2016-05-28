package com.pandu.patpat.android.staftrack;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends AppCompatActivity {

    ExpandableRelativeLayout expandableLayout1;
    String nama_text;
    ProfilDAO mProfilDAO;
    ListView listview;
    ListProfilAdapter adapter = null;
    ArrayList<HashMap<String, String>> arraylist;
    Boolean internet_error = false;

    ImageView send;
    TextView keperluan;

    String keperluan_text;

    String id_orang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        send = (ImageView) findViewById(R.id.send_btn);
        keperluan = (TextView) findViewById(R.id.keperluan);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(keperluan.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Keperluan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {

                    keperluan_text = keperluan.getText().toString();

                    String keperluanArray[] = keperluan_text.split(" ");

                    if (keperluanArray.length > 1) {

                        keperluan_text = "";

                        for (int i = 0; i < keperluanArray.length - 1; i++) {

                            keperluan_text += keperluanArray[i] + "%20";

                        }
                        keperluan_text += keperluanArray[keperluanArray.length - 1];

                        buatKeperluan(String.valueOf(mProfilDAO.getTopProfil().getIdprofil()), id_orang, mProfilDAO.getTopProfil().getJabatan_profil(), keperluan_text);
                    }
                }

            }
        });

        if(getSupportActionBar() != null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);}

        final Button kontakbtn = (Button) findViewById(R.id.hubungi);
        if (kontakbtn != null) {
            kontakbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(ContactActivity.this, kontakbtn);
                    popup.getMenuInflater().inflate(R.menu.menu_send, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId())
                            {
                                case R.id.Email:
                                    sendEmail();
                                    break;

                                case R.id.sms:
                                    sendSMS();
                                    break;
                            }
                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }
            });
        }

        Intent a = getIntent();
        nama_text = a.getExtras().getString("nama");

        String namaArray[] = nama_text.split(" ");

        if(namaArray.length > 1) {

            nama_text = "";

            for (int i = 0 ; i < namaArray.length - 1 ; i++) {

                nama_text += namaArray[i] + "%20";

            }

            nama_text += namaArray[namaArray.length - 1];

        }

        mProfilDAO = new ProfilDAO(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "http://patpatstudio.com/staftrek/detail.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&id=" + id_text, Toast.LENGTH_LONG).show();

        listview = (ListView) findViewById(R.id.listViewlay);

        NetworkUtils utils = new NetworkUtils(getApplicationContext());

        if(utils.isConnectingToInternet()) {

            new DownloadJSON().execute();

        }
        else {

            Toast.makeText(getApplicationContext(), "No internet connectionxxx", Toast.LENGTH_SHORT).show();

        }

    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("01234"));
        smsIntent.putExtra("sms_body"  , "Test ");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending ...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        if(expandableLayout1 != null)
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //haha.setVisibility(View.VISIBLE);
            super.onPreExecute();
            //pDialog = ProgressDialog.show(getActivity(), "Please wait...", "Silahkan tunggu");
            //progressDialog = createProgressDialog(getActivity());
            //progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylist = new ArrayList<HashMap<String, String>>();

            String serverData = null;

            if(!internet_error) {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/detail.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&id=" + nama_text );
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    serverData = EntityUtils.toString(httpEntity);
                    Log.d("response", serverData);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    internet_error = true;
                    this.cancel(true);
                }
            }
            else {
                internet_error = true;
                this.cancel(true);
            }

            if(!internet_error){

                try {
                    // Locate the array name in JSON
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonarray = jsonObject.getJSONArray("detail");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObjectBahan = jsonarray.getJSONObject(i);
                        // Retrive JSON Objects

                        map.put("id", jsonObjectBahan.getString("id"));
                        id_orang = jsonObjectBahan.getString("id");
                        map.put("nama", jsonObjectBahan.getString("nama"));
                        map.put("foto", jsonObjectBahan.getString("foto"));
                        map.put("no", jsonObjectBahan.getString("no"));
                        map.put("hp", jsonObjectBahan.getString("hp"));
                        map.put("email", jsonObjectBahan.getString("email"));
                        map.put("alamat", jsonObjectBahan.getString("alamat"));
                        map.put("unit", jsonObjectBahan.getString("unit"));
                        // Set the JSON Objects into the array
                        arraylist.add(map);
                    }
                } catch (JSONException e) {
                    this.cancel(true);
                    internet_error = true;
                    e.printStackTrace();
                }


            }
            else {

                this.cancel(true);
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            internet_error = true;
            super.onCancelled();
            //progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Internet connection error xxx", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void args) {
            listview.setVisibility(View.VISIBLE);
            // Pass the results into ListViewAdapter.java
            if(internet_error) {

                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Internet connection error", Toast.LENGTH_SHORT).show();

            }
            else {

                adapter = new ListProfilAdapter(ContactActivity.this, arraylist);
                // Set the adapter to the ListView
                listview.setAdapter(adapter);
                // Close the progress dialog
                //progressDialog.dismiss();
            }

        }
    }

    private void buatKeperluan(final String id_saya, final String id_orang, final String jabatan, final String keperluan) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getBaseContext(), "http://patpatstudio.com/staftrek/tambahtrek.php?jabatan=" + jabatan + "&idp=" + id_saya + "&idd=" + id_orang + "&keperluan=" + keperluan, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String versi = params[0];
                String tgl = params[1];

                InputStream is = null;
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/tambahtrek.php?jabatan=" + jabatan + "&idp=" + id_saya + "&idd=" + id_orang + "&keperluan=" + keperluan);

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

                Toast.makeText(getBaseContext(), "Keperluan telah dibuat", Toast.LENGTH_SHORT).show();
                finish();

            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(id_saya, id_orang, jabatan, keperluan);

    }


}
