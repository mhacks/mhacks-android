package com.mhacks.android.ui.common;

/**
 * Created by Riyu on 11/12/14.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mhacks.android.data_old.model.Sponsor;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Sponsor> url;
    private ImageLoader il;
    //private ImageView imageView;

    public ImageAdapter(Context c, ArrayList<Sponsor> urls) {
        mContext = c;
        il = new ImageLoader(c);
        url = new ArrayList<Sponsor> ();
        for (int i = 0; i < urls.size(); ++i){
            url.add(urls.get(i));
        }
    }

    public int getCount() {
        return url.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(110, 83));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        il.DisplayImage(url.get(position).getLogo().getUrl() , imageView);

        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}