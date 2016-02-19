package com.mhacks.android.ui.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import org.mhacks.android.R;

import javax.xml.transform.Result;


/**
 * Created by Ankit on 2/4/16.
 */
public class GroundOverlayLoader extends AsyncTask<Object, Object, Object>{

    private OnTaskCompleted onTaskCompleted;
    private GroundOverlayOptions options;
    private Activity activity;
    LatLngBounds bounds;


    public GroundOverlayLoader(OnTaskCompleted listener, Activity activity_in, LatLngBounds bounds_in){
        onTaskCompleted = listener;
        activity = activity_in;
        bounds = bounds_in;
    }

    @Override
    protected Object doInBackground(Object... args){
        options = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeStream(activity
                        .getResources().openRawResource(R.raw.map_no_edges))))
                .positionFromBounds(bounds);
        return null;
    }

    @Override
    protected void onPostExecute(Object result){
        onTaskCompleted.onTaskCompleted(options);
    }
}
