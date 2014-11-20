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
import com.mhacks.iv.android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> url;

    public ImageAdapter(Context c, ArrayList<String> urls) {
        mContext = c;
        url = new ArrayList<String> ();
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
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        new ImageDownloader(imageView).execute(url.get(position));
        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}