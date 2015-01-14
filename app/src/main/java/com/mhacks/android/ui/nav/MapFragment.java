package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mhacks.android.data.model.Map;
import com.mhacks.android.ui.common.ImageLoader;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 12/31/2014.
 *
 * Displays maps of the MHacks V venues.
 */
public class MapFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "MapFragment";

    public static final String MAP_PIN = "mapPin";

    //Views
    private View      mMapFragView;
    private ImageView mapView;

    //Spinner
    private Spinner mapSpinner;

    private ArrayList<String> mapNames;
    private ArrayList<Map> maps;

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
    }

    /**
     * Queries the local datastore for Map objects.
     */
    private void getLocalMaps() {
        ParseQuery<Map> query = ParseQuery.getQuery("Map");
        query.fromPin(MAP_PIN);
        query.findInBackground(new FindCallback<Map>() {
            @Override
            public void done(List<Map> mapList, ParseException e) {
                if (e == null) {
                    if (mapList.size() == 0) {
                        getRemoteMaps();
                    } else {

                        //Fill them with empty strings.
                        for(int i = 0; i < mapList.size(); i++) {
                            mapNames.add("");
                        }

                        //Instantiate maps as a copy of mapList
                        maps = new ArrayList<Map>(mapList);

                        for (Map m : mapList) {
                            maps.set(m.getOrder()-1, m);
                            mapNames.set(m.getOrder()-1, m.getTitle());
                        }
                        setAdapter();
                    }
                } else {
                    Log.e(TAG, "Query failed.", e);
                }
            }
        });
    }

    /**
     * Queries the Parse database for Map objects.
     */
    private void getRemoteMaps () {
        ParseQuery<Map> query = ParseQuery.getQuery("Map");
        query.findInBackground(new FindCallback<Map>() {
            @Override
            public void done(List<Map> mapList, ParseException e) {
                if (e == null) {
                    //Pin list to local datastore.
                    ParseObject.unpinAllInBackground(MAP_PIN, mapList);
                    ParseObject.pinAllInBackground(MAP_PIN, mapList);

                    //Fill them with empty strings.
                    for(int i = 0; i < mapList.size(); i++) {
                        mapNames.add("");
                    }

                    //Instantiate maps as a copy of mapList
                    maps = new ArrayList<Map>(mapList);

                    for (Map m : mapList) {
                        maps.set(m.getOrder()-1, m);
                        mapNames.set(m.getOrder()-1, m.getTitle());
                    }
                    setAdapter();
                } else {
                    Log.e(TAG, "Query failed.", e);
                }
            }
        });
    }

    /**
     * Sets the String array adapter to the names of the maps pulled from Parse. These are displayed
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
        maps.get(position).getImage().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                mapView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
