package com.mhacks.android.ui.map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.mhacks.android.data.model.MapLocation;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/6/14.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements GoogleMap.OnMapLoadedCallback {
  public static final String TAG = "MapFragment";

  private List<MapLocation> mLocations = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      mLocations = MapLocation.query().find();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    getMap().setMyLocationEnabled(true);
    getMap().getUiSettings().setZoomControlsEnabled(false);
    getMap().getUiSettings().setAllGesturesEnabled(true);
    getMap().setOnMapLoadedCallback(this);
    centerOnVenue();
  }

  public void centerOnVenue() {
    new BoundsBuilderTask() {
      @Override
      protected void onPostExecute(CameraUpdate cameraUpdate) {
        getMap().moveCamera(cameraUpdate);
      }
    }.execute(mLocations.toArray(new MapLocation[mLocations.size()]));
  }

  @Override
  public void onMapLoaded() {
    GoogleMap map = getMap();

    for (MapLocation location : mLocations) {
      PolygonOptions polygon = new PolygonOptions();
      polygon.addAll(location.getGeometry());

      map.addPolygon(polygon);
    }
  }

  private static class BoundsBuilderTask extends AsyncTask<MapLocation, Void, CameraUpdate> {

    @Override
    protected CameraUpdate doInBackground(MapLocation... mapLocations) {
      final LatLngBounds.Builder builder = LatLngBounds.builder();
      for (MapLocation location : mapLocations) {
        for (LatLng latLng : location.getGeometry()) {
          builder.include(latLng);
        }
      }

      return CameraUpdateFactory.newLatLngBounds(builder.build(), 0);
    }

  }
}
