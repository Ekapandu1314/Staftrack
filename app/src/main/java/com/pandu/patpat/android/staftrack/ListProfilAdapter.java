package com.pandu.patpat.android.staftrack;

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
public class ListProfilAdapter extends BaseAdapter {

    // Declare Variables



    Context context;
    ProfilDAO mProfilDAO;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListProfilAdapter(Context context,
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

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.dialog_contact, parent, false);
        // Get the position
        resultp = data.get(position);


        TextView id = (TextView) itemView.findViewById(R.id.aidi_profil_profil);
        TextView name = (TextView) itemView.findViewById(R.id.name_profil_profil);
        TextView jabatan = (TextView) itemView.findViewById(R.id.jabatan_profil_profil);
        TextView nmr_induk = (TextView) itemView.findViewById(R.id.nmr_induk_profil_profil);
        TextView unitt = (TextView) itemView.findViewById(R.id.unit_profil_profil);
        TextView nmr_telp = (TextView) itemView.findViewById(R.id.nmr_telp_profil_profil);
        TextView imel = (TextView) itemView.findViewById(R.id.imel_profil_profil);
        TextView alamatt = (TextView) itemView.findViewById(R.id.alamat_profil_profil);
        ImageView gambar = (ImageView) itemView.findViewById(R.id.foto_profil_profil);

        // Capture position and set results to the TextViews
        id.setText(resultp.get("id"));
        name.setText(resultp.get("nama"));
        nmr_induk.setText(resultp.get("no"));
        unitt.setText(resultp.get("unit"));
        nmr_telp.setText(resultp.get("hp"));
        imel.setText(resultp.get("email"));
        alamatt.setText(resultp.get("alamat"));
        // Capture position and set results to the ImageView
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(resultp.get("foto"), gambar, options, animateFirstListener);

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
