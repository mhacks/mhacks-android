package com.mhacks.android.ui.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import org.mhacks.android.R;

import javax.xml.transform.Result;


/**
 * Created by Ankit on 2/4/16.
 */
public class GroundOverlayLoader extends AsyncTask<Object, Object, Object>{

    private final String TAG = "GroundOverlayLoader";
    private OnTaskCompleted onTaskCompleted;
    private GroundOverlayOptions options;
    private Activity activity;
    private MapFragment mapFragment;
    private LatLngBounds bounds;
    private boolean success = true;

    public GroundOverlayLoader(OnTaskCompleted listener,
                               Activity activity,
                               LatLngBounds bounds,
                               MapFragment mapFragment){
        onTaskCompleted = listener;
        this.activity = activity;
        this.bounds = bounds;
        this.mapFragment = mapFragment;
    }

    @Override
    protected Object doInBackground(Object... args){
        try{
            BitmapFactory.Options bMapOptions = new BitmapFactory.Options();
            bMapOptions.inJustDecodeBounds = false;
            bMapOptions.inSampleSize = 4;
            options = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(activity
                            .getResources(), R.drawable.grand_map, bMapOptions)))
                    .positionFromBounds(bounds);
        } catch (NullPointerException e){
            Log.e(TAG, "Activity was destroyed");
            success = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result){
        if(success) onTaskCompleted.onTaskCompleted(options, mapFragment);
    }
}
