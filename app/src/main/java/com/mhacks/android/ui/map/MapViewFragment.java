package com.mhacks.android.ui.map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mhacks.android.data.model.Location;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Omkar Moghe on 12/31/2014.
 *
 * Displays mMaps of the MHacks V venues.
 */
public class MapViewFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, OnMapReadyCallback, OnTaskCompleted{

    public static final String TAG = "MapViewFragment";
    public static final String MAP_PIN = "mapPin";
    private ArrayList<Marker> mMarkers = new ArrayList<>();

    //Views
    private View mMapFragView;
    private boolean created = false;
    private ReentrantLock mapLock = new ReentrantLock();
    private boolean mapReady = false;
    @Nullable
    private GoogleMap gMap = null;
    @Nullable
    private GroundOverlayOptions option = null;

    private static final LatLng NORTHEAST = new LatLng(42.29353, -83.713641);
    private static final LatLng SOUTHWEST = new LatLng(42.29182, -83.716611);
    private static final LatLngBounds CORNERS = new LatLngBounds(SOUTHWEST, NORTHEAST);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if(!created) {
            mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);
        }
        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        for(Marker _marker: mMarkers){
            _marker.remove();
        }
        mMarkers.clear();
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap map){
        Log.d(TAG, "Map Ready");
        if(map == null){
            Log.e(TAG, "Map is null!");
            return;
        }
        gMap = map;
        //TODO: Make initial position modular
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.2919466, -83.7153427), 16));

        mapLock.lock();
        Log.d(TAG, "Lock acquired by mapready");
        mapReady = true;
        if(option != null){
            gMap.addGroundOverlay(option);
        }
        mapLock.unlock();

        updateCameraWithLocations();

        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        //TODO: stop it from complaining about disabled permissions
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            gMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onTaskCompleted(GroundOverlayOptions options){
        mapLock.lock();
        Log.d(TAG, "Lock acquired by task completed");
        option = options;
        if(mapReady){
            if(gMap == null){
                Log.e(TAG, "Map is null!");
            }
            gMap.addGroundOverlay(option);
        }
        mapLock.unlock();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onDestroy() {
        created = false;
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!created) {
            try {
                // for BitmapFactory
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    MapFragment mapFragment = MapFragment.newInstance();
                    FragmentManager fm = getChildFragmentManager();
                    mMapFragView.findViewById(R.id.loading_bar).setVisibility(View.GONE);
                    fm.beginTransaction().replace(R.id.map_view_container, mapFragment).commit();
                    if (mapFragment == null) {
                        Log.e(TAG, "Could not get Fragment");
                        //TODO: some sort of error code in the UI
                    } else {
                        mapFragment.getMapAsync(MapViewFragment.this);
                        if(option == null){
                            GroundOverlayLoader loader = new GroundOverlayLoader(MapViewFragment.this, MapViewFragment.this.getActivity(), CORNERS);
                            loader.execute();
                        }

                    }
                }
            }, 1000);

            created = true;
        }
        else{
            updateCameraWithLocations();
        }
    }

    private void updateCameraWithLocations(){
        if(gMap == null){
            Log.e(TAG, "Map was null!");
            return;
        }
        ArrayList<Location> _locations = LocationsQueue.locations;
        float zoom = (float) 16.0;
        if(!_locations.isEmpty()){
            double swLat = 42.287503;
            double swLong = -83.718795;
            for (int i = 0; i < _locations.size(); i++){
                if(_locations.get(i).getLatitude() < swLat || _locations.get(i).getLongitude() < swLong) zoom = (float) 13.25;
                Marker _marker = gMap.addMarker(new MarkerOptions().position(new LatLng(_locations.get(i).getLatitude(),
                        _locations.get(i).getLongitude())).title(_locations.get(i).getName()));
                mMarkers.add(_marker);
            }

            _locations.clear();
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.2919466, -83.7153427), zoom));
    }
}
