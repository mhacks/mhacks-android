package com.mhacks.android.ui.map;

import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.network.HackathonCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/7/2016.
 */

public class LocationManager {
    private ArrayList<Location> locations;
    private static LocationManager instance;

    public static LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }

        return instance;
    }

    private LocationManager () {
//        NetworkManager networkManager = NetworkManager.getInstance();
//        networkManager.getLocations(new HackathonCallback<List<Location>>() {
//            @Override
//            public void success(List<Location> response) {
//                locations = new ArrayList<Location>(response);
//            }
//
//            @Override
//            public void failure(Throwable error) {
//                locations = new ArrayList<Location>();
//            }
//        });
    }

    public static Location getLocation(String id) {
        LocationManager manager = LocationManager.getInstance();

        for (Location l : manager.locations) {
            if (l.getId().equals(id)) return l;
        }

        return null;
    }
}
