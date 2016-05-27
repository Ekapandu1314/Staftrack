package com.pandu.patpat.android.staftrack;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class SearchResultActivity extends AppCompatActivity {

    String keywords;
    ProgressBar haha;

    ListView listview;
    ListSearchAdapter adapter = null;
    ArrayList<HashMap<String, String>> arraylist;

    public static String NAMA = "nama";
    public static String NIM = "no";
    public static String UNIT = "unit";
    public static String GAMBAR = "foto";
    public static String AIDI = "id";

    Boolean internet_error = false;

    ProfilDAO mProfilDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mProfilDAO = new ProfilDAO(getApplicationContext());

        Intent i = getIntent();
        keywords = i.getExtras().getString("keywords");
        Toast.makeText(getApplicationContext(), keywords, Toast.LENGTH_SHORT).show();

        haha = (ProgressBar) findViewById(R.id.progress_search);
        listview = (ListView) findViewById(R.id.listViewSearch);

        NetworkUtils utils = new NetworkUtils(getApplicationContext());
        if(utils.isConnectingToInternet()) {

                new DownloadJSON().execute();

        }
        else {

            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();

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
                HttpGet httpGet = new HttpGet("http://patpatstudio.com/staftrek/cari.php?jabatan=" + mProfilDAO.getTopProfil().getJabatan_profil() + "&keyword=" + keywords);
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
                    JSONArray jsonarray = jsonObject.getJSONArray("cari");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObjectBahan = jsonarray.getJSONObject(i);
                        // Retrive JSON Objects
                        map.put(AIDI, jsonObjectBahan.getString(AIDI));
                        map.put(NAMA, jsonObjectBahan.getString(NAMA));
                        map.put(NIM, jsonObjectBahan.getString(NIM));
                        map.put(GAMBAR, jsonObjectBahan.getString(GAMBAR));
                        map.put(UNIT, jsonObjectBahan.getString(UNIT));
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
            haha.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Internet connection error", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void args) {
            listview.setVisibility(View.VISIBLE);
            // Pass the results into ListViewAdapter.java
            if(internet_error) {

                //progressDialog.dismiss();
                haha.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Internet connection error", Toast.LENGTH_SHORT).show();

            }
            else {

                adapter = new ListSearchAdapter(SearchResultActivity.this, arraylist);
                // Set the adapter to the ListView
                listview.setAdapter(adapter);
                // Close the progress dialog
                //progressDialog.dismiss();
                haha.setVisibility(View.GONE);

            }

        }
    }
}
