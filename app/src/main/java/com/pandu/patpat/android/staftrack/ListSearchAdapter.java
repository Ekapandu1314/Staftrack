package com.pandu.patpat.android.staftrack;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Eka Pandu Winata on 5/27/2016.
 */
public class ListSearchAdapter extends BaseAdapter {

    // Declare Variables



    Context context;
    ProfilDAO mProfilDAO;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListSearchAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView nama;
        TextView nim;
        TextView unit;
        TextView aidi;
        TextView lat;
        TextView lon;
        ImageView flag;
        Button profil;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item_search, parent, false);
        // Get the position
        resultp = data.get(position);


        nama = (TextView) itemView.findViewById(R.id.nama_search);
        nim = (TextView) itemView.findViewById(R.id.country);
        unit = (TextView) itemView.findViewById(R.id.unit_search);
        aidi = (TextView) itemView.findViewById(R.id.aidi);
        lat = (TextView) itemView.findViewById(R.id.latSearch);
        lon = (TextView) itemView.findViewById(R.id.lonSearch);
        profil = (Button) itemView.findViewById(R.id.profilSearch);


        flag = (ImageView) itemView.findViewById(R.id.flag);

        // Capture position and set results to the TextViews
        nama.setText(resultp.get(SearchResultActivity.NAMA));
        nim.setText("No = " + resultp.get(SearchResultActivity.NIM));
        unit.setText(resultp.get(SearchResultActivity.UNIT));
        aidi.setText(resultp.get(SearchResultActivity.AIDI));
        lat.setText(resultp.get("la"));
        lon.setText(resultp.get("lo"));
        // Capture position and set results to the ImageView
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(resultp.get(SearchResultActivity.GAMBAR), flag, options, animateFirstListener);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfilLengkap.class);
                Bundle search = new Bundle();
                search.putString("nama", resultp.get(SearchResultActivity.NAMA));
                intent.putExtras(search);
                context.startActivity(intent);

            }

        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.searchresult.finish();
                MapActivity mapActivity = new MapActivity();
                mapActivity.setMarker(resultp.get(SearchResultActivity.NAMA), Double.parseDouble(resultp.get("la")), Double.parseDouble(resultp.get("lo")));
            }
        });

        // Capture ListView item click
//        itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // Get the position
//                resultp = data.get(position);
//                Intent intent = new Intent(context, BukaArtikel.class);
//                // Pass all data rank
//                intent.putExtra("judul", resultp.get(InfoTips.JUDUL));
//                // Pass all data country
//                intent.putExtra("deskripsi", resultp.get(InfoTips.DESKRIPSI));
//                // Pass all data population
//                intent.putExtra("link",resultp.get(InfoTips.LINK));
//                // Pass all data flag
//                intent.putExtra("gambar", resultp.get(InfoTips.GAMBAR));
//
//                context.startActivity(intent);
//
//            }
//        });
        return itemView;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}