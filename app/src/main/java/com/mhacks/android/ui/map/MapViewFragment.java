package com.mhacks.android.ui.map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.mhacks.android.ui.MainActivity;
import org.mhacks.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 12/31/2014.
 *
 * Displays mMaps of the MHacks V venues.
 */
public class MapViewFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnMapReadyCallback{

    public static final String TAG = "MapViewFragment";
    public static final String MAP_PIN = "mapPin";

    //Views
    private View      mMapFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        //TODO: stop it from complaining about API 14 http://stackoverflow.com/questions/26592889/mapfragment-or-mapview-getmap-returns-null-on-lollipop#answer-27681586
        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view_container);

        if(mapFragment == null){
            Log.e(TAG, "Could not get Fragment");
            //TODO: some sort of error code in the UI
        }
        else{
            mapFragment.getMapAsync(this);
        }

        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        //TODO: stop it from complaining about API 14 http://stackoverflow.com/questions/26592889/mapfragment-or-mapview-getmap-returns-null-on-lollipop#answer-27681586
        FragmentManager fm = getChildFragmentManager();
        Fragment mapFragment = fm.findFragmentById(R.id.map_view_container);
        if (mapFragment != null) {
            fm.beginTransaction().remove(mapFragment).commit();
        }
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap map){
        Log.d(TAG, "Map Ready");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
