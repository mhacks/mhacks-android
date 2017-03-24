package com.mhacks.android.ui.map;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mhacks.android.data.model.Floor;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.ui.MainActivity;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anksh on 12/31/2014.
 * Updated by omkarmoghe on 10/6/16
 *
 * Displays maps of the MHacks 8 venues.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapViewFragment";

    // Views
    private    View      mMapFragView;
    public     Spinner  nameView;

    // Map
    private MapFragment mMapFragment;
    private GoogleMap   mGoogleMap;

    // Data
    ArrayList<Floor> floors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (mMapFragView == null) mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        nameView = (Spinner) mMapFragView.findViewById(R.id.name);

        setUpMapIfNeeded();

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getFloors(new HackathonCallback<List<Floor>>() {
            @Override
            public void success(List<Floor> response) {
                floors = new ArrayList<>(response);

                ArrayAdapter<String> spinnerAdapter =
                        new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                if (!floors.isEmpty()) {
                    for (Floor floor : floors) {

                        spinnerAdapter.add(floor.getName());

                    }
                    nameView.setAdapter(spinnerAdapter);
                    spinnerAdapter.notifyDataSetChanged();

                    nameView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView,
                                                   View view,
                                                   int i,
                                                   long l) {
                            addOverlay(floors.get(i));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void failure(Throwable error) {
                Log.e(TAG, "unable to get floors", error);
            }
        });


        return mMapFragView;
    }

    private void setUpMapIfNeeded() {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.map, mMapFragment).commit();
        }

        if (mGoogleMap == null) mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setBuildingsEnabled(true);

        UiSettings settings = mGoogleMap.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setTiltGesturesEnabled(true);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(42.292650, -83.714359));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);
    }

    private void addOverlay(final Floor floor) {
        setUpMapIfNeeded();

        // Grab bitmap image
        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getImage(floor.getImage(), new HackathonCallback<Bitmap>() {
            @Override
            public void success(Bitmap response) {
                final Bitmap image = response;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LatLngBounds northCampusBounds = new LatLngBounds(
                                // I'm a dumbass so we gotta flip the latitudes
                                new LatLng(floor.getSeLatitude(), floor.getNwLongitude()), // South west corner
                                new LatLng(floor.getNwLatitude(), floor.getSeLongitude())  // North east corner
                        );

                        GroundOverlayOptions northCampusMap = new GroundOverlayOptions()
                                .image(BitmapDescriptorFactory.fromBitmap(image))
                                .positionFromBounds(northCampusBounds);

                        mGoogleMap.addGroundOverlay(northCampusMap);
                    }
                });
            }

            @Override
            public void failure(Throwable error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
