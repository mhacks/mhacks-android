package com.mhacks.android.ui.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


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
public class MapViewFragment extends Fragment implements AdapterView.OnItemSelectedListener{

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


        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
