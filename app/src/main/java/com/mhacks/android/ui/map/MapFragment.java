package com.mhacks.android.ui.map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.common.collect.Lists;
import com.mhacks.iv.android.R;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/6/14.
 */
public class MapFragment extends SupportMapFragment implements GoogleMap.OnMapLoadedCallback {
  public static final String TAG = "MapFragment";

  public static final int OPACITY = 0x88FFFFFF;

  public int mStrokeColor = 0xFFFFFFFF; // let's not use this one though
  public float mStrokeWidth = 1.0f; // yeah...

  private List<Venue> mVenues = Lists.newArrayList();
  private CameraUpdate mCenter;
  private boolean mLoaded = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      mVenues = Venue.query().find();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    mStrokeColor = getResources().getColor(R.color.charcoal_semitransparent);
    mStrokeWidth = getResources().getDimension(R.dimen.map_polygon_stroke_width);
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
        mCenter = cameraUpdate;
        if (getMap() != null) {
          getMap().moveCamera(cameraUpdate);
          mLoaded = true;
        }
      }
    }.execute(mVenues.toArray(new Venue[mVenues.size()]));
  }

  @Override
  public void onMapLoaded() {
    GoogleMap map = getMap();

    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    if (!mLoaded && mCenter != null){
      map.moveCamera(mCenter);
      mLoaded = true;
    }

    addPolygons(map);
    addMarkers(map);
  }

  private void addPolygons(GoogleMap map) {
    for (Venue venue : mVenues) {
      List<LatLng> geometry = venue.getGeometry();
      if (geometry != null) {
        map.addPolygon(new PolygonOptions()
          .addAll(geometry)
          .fillColor(venue.getColor() & OPACITY)
          .strokeColor(mStrokeColor)
          .strokeWidth(mStrokeWidth));
      }
    }
  }

  public void addMarkers(GoogleMap map) {
    for (Venue venue : mVenues) {
      float[] hsv = new float[3];
      Color.colorToHSV(venue.getColor(), hsv);
      map.addMarker(new MarkerOptions()
        .position(venue.getLocation())
        .title(venue.getTitle())
        .snippet(venue.getDetails())
        .icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
    }
  }

  private static class BoundsBuilderTask extends AsyncTask<Venue, Void, CameraUpdate> {

    @Override
    protected CameraUpdate doInBackground(Venue... venues) {
      final LatLngBounds.Builder builder = LatLngBounds.builder();
      for (Venue location : venues) {
        for (LatLng latLng : location.getGeometry()) {
          builder.include(latLng);
        }
      }

      return CameraUpdateFactory.newLatLngBounds(builder.build(), 0);
    }

  }
}
