package com.mhacks.android.ui.nav;

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
import android.widget.Spinner;

import com.mhacks.android.data.model.Map;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 12/31/2014.
 *
 * Displays mMaps of the MHacks V venues.
 */
public class MapFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "MapFragment";
    public static final String MAP_PIN = "mapPin";

    //Views
    private View      mMapFragView;
    private ImageView mapView;

    //Spinner
    private Spinner mapSpinner;

    //Current query
    private ParseQuery<Map> currentQuery;

    private ArrayList<String> mapNames;
    private ArrayList<Map> mMaps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        //Instantiate lists.
        mapNames = new ArrayList<>();

        //Inflate spinner view.
        mapSpinner = (Spinner) getActivity().findViewById(R.id.map_spinner);
        mapSpinner.setVisibility(View.VISIBLE);
        mapSpinner.setOnItemSelectedListener(this);

        mapView = (ImageView) mMapFragView.findViewById(R.id.map_view);

        getLocalMaps();

        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapSpinner.setVisibility(View.GONE);
        if (currentQuery != null) currentQuery.cancel();
    }

    private ParseQuery<Map> getBaseQuery() {
        ParseQuery<Map> query = ParseQuery.getQuery("Map");
        currentQuery = query;
        return query;
    }

    /**
     * Queries the local datastore for Map objects.
     */
    private void getLocalMaps() {
        ParseQuery<Map> query = getBaseQuery();
        query.fromPin(MAP_PIN);
        query.findInBackground(new FindCallback<Map>() {
            @Override
            public void done(List<Map> mapList, ParseException e) {
                if (e != null || mapList == null || mapList.size() <= 0) {
                    Log.e(TAG, "Couldn't get the local mMaps, falling back on remote");
                } else {
                    Log.d(TAG, "Got the local mMaps");
                    processMapList(mapList);
                }

                getRemoteMaps();
            }
        });
    }

    private void processMapList(List<Map> mapList) {
        //Fill them with empty strings.
        mapNames = new ArrayList<String>();
        for(int i = 0; i < mapList.size(); i++) {
            mapNames.add("");
        }

        //Instantiate mMaps as a copy of mapList
        mMaps = new ArrayList<Map>(mapList);
        for (Map m : mapList) {
            mMaps.set(m.getOrder(), m);
            mapNames.set(m.getOrder(), m.getTitle());
        }

        setAdapter();
    }

    /**
     * Queries the Parse database for Map objects.
     */
    private void getRemoteMaps () {
        ParseQuery<Map> query = ParseQuery.getQuery("Map");
        query.findInBackground(new FindCallback<Map>() {
            @Override
            public void done(List<Map> mapList, ParseException e) {
                if(e != null || mapList == null || mapList.size() <= 0) {
                    Log.e(TAG, "Couldn't get the remote mMaps");

                    // We don't have anything locally, let the user know we need internet
                    if(mMaps == null || mMaps.size() <= 0) {
                        if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                    } else {
                        // We do have local stuff, no make sure we aren't in the way
                        if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                    }
                } else {
                    Log.d(TAG, "Got the remote mMaps, displaying them");

                    // We got the remote mMaps, so unpin the old ones and pin the new ones
                    ParseObject.unpinAllInBackground(MAP_PIN);
                    ParseObject.pinAllInBackground(MAP_PIN, mapList);

                    // Display the new mMaps and get outta the way
                    processMapList(mapList);
                    if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                }
            }
        });
    }

    /**
     * Sets the String array adapter to the names of the mMaps pulled from Parse. These are displayed
     * in the spinner on the SupportActionBar (Toolbar).
     */
    public void setAdapter () {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                                                       android.R.layout.simple_spinner_item,
                                                                       mapNames);
        spinnerAdapter.setDropDownViewResource(R.layout.map_spinner_item);
        mapSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Picasso.with(getActivity()).load(mMaps.get(position).getImage().getUrl()).into(mapView);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
