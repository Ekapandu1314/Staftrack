package com.pandu.patpat.android.staftrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Eka Pandu Winata on 5/28/2016.
 */
public class ListTrekingAdapter extends BaseAdapter {

    // Declare Variables



    Context context;
    ProfilDAO mProfilDAO;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListTrekingAdapter(Context context,
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
        TextView status;
        ImageView flag;
        Button haha;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item_trekking, parent, false);
        // Get the position
        resultp = data.get(position);

        nama = (TextView) itemView.findViewById(R.id.name_trekking);
        nim = (TextView) itemView.findViewById(R.id.no_trekking);
        unit = (TextView) itemView.findViewById(R.id.unit_trekking);
        aidi = (TextView) itemView.findViewById(R.id.aidi_trekking);
        status = (TextView) itemView.findViewById(R.id.status_trekking);

        flag = (ImageView) itemView.findViewById(R.id.foto_trekking);

        // Capture position and set results to the TextViews
        aidi.setText(resultp.get("id"));
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(resultp.get("foto"), flag, options, animateFirstListener);

        nama.setText(resultp.get("nama"));
        nim.setText("No = " + resultp.get("no"));
        unit.setText(resultp.get("unit"));
        status.setText(resultp.get("status"));

        //unit.setVisibility(View.VISIBLE);
        //status.setVisibility(View.VISIBLE);


        // Capture position and set results to the ImageView


//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent();
//                intent.putExtra("nama",resultp.get(SearchResultActivity.NAMA));
//                intent.putExtra("lat",resultp.get("la"));
//                intent.putExtra("lon",resultp.get("lo"));
//                SearchResultActivity.searchresult.setResult(Activity.RESULT_OK, intent);
//                SearchResultActivity.searchresult.finish();
//            }
//        });

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
