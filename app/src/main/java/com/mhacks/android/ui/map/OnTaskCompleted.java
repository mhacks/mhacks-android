package com.mhacks.android.ui.map;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.GroundOverlayOptions;

/**
 * Created by Ankit on 2/4/16.
 */
public interface OnTaskCompleted {
    public void onTaskCompleted(GroundOverlayOptions options, MapFragment mapFragment);
}
