package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mhacks.android.data.model.Map;
import com.mhacks.android.ui.common.ImageLoader;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 12/31/2014.
 *
 * Displays maps of the MHacks V venues.
 */
public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";

    //Views
    private View      mMapFragView;
    private ImageView mapView;

    //Map object list
    private List<Map> maps;

    //ImageLoader
    ImageLoader imageLoader;

    private String[] tabs = {"ALL", "BBB", "EECS", "GG Brown"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (ImageView) mMapFragView.findViewById(R.id.map_view);

        getMaps();

        return mMapFragView;
    }

    /**
     * Queries the Parse database for Map objects.
     */
    public void getMaps() {
        ParseQuery<Map> query = ParseQuery.getQuery("Map");
        query.findInBackground(new FindCallback<Map>() {
            @Override
            public void done(List<Map> mapsList, ParseException e) {
                if (e == null) {
                    maps = new ArrayList<Map>(mapsList);
//                    imageLoader.DisplayImage(maps.get(0).getImage().getUrl(), mapView);
                    Log.v(TAG, maps.size() + " maps");
                } else {
                    Log.e(TAG, "Query failed.", e);
                }
            }
        });
    }
}
