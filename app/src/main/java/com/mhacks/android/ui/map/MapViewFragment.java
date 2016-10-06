package com.mhacks.android.ui.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mhacks.android.R;

/**
 * Created by anksh on 12/31/2014.
 * Updated by omkarmoghe on 10/6/16
 *
 * Displays maps of the MHacks 8 venues.
 */
public class MapViewFragment extends Fragment {

    public static final String TAG = "MapViewFragment";

    //Views
    private View mMapFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);
        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
