package com.pandu.patpat.android.staftrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfilLengkap extends AppCompatActivity {

    String nama_text;
    ProfilDAO mProfilDAO;
    ListView listview;
    ListProfilAdapter adapter = null;
    ArrayList<HashMap<String, String>> arraylist;
    Boolean internet_error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_lengkap);

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

        Toast.makeText(getApplicationContext(), nama_text, Toast.LENGTH_SHORT).show();

        mProfilDAO = new ProfilDAO(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "http://patpatstudio.com/staftrek/detail.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&id=" + id_text, Toast.LENGTH_LONG).show();

        listview = (ListView) findViewById(R.id.listViewProfil);

        NetworkUtils utils = new NetworkUtils(getApplicationContext());

        if(utils.isConnectingToInternet()) {

            new DownloadJSON().execute();

        }
        else {

            Toast.makeText(getApplicationContext(), "No internet connectionxxx", Toast.LENGTH_SHORT).show();

        }
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

                adapter = new ListProfilAdapter(ProfilLengkap.this, arraylist);
                // Set the adapter to the ListView
                listview.setAdapter(adapter);
                // Close the progress dialog
                //progressDialog.dismiss();
            }

        }
    }


}
