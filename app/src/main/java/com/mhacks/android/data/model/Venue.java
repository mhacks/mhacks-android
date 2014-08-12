package com.mhacks.android.data.model;

import android.os.Parcel;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.maps.android.SphericalUtil;
import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(Venue.CLASS)
public class Venue extends DataClass<Venue> {
  public static final String CLASS = "Venue";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String IMAGE = "image";
  public static final String COLOR = "color";
  public static final String LOCATION = "location";
  public static final String BOUNDS = "bounds";

  public static final int LOITERING_DELAY = 300000; // five minutes
  public static final int NOTIFICATION_RESPONSIVENESS = 150000;

  public Venue() {
    super(false);
  }

  public Venue(String title, String details, ParseGeoPoint location) {
    super(true);

    setTitle(title);
    setDetails(details);
    setLocation(location);
  }

  public String getTitle() {
    return getString(TITLE);
  }

  public Venue setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public Venue setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public ParseFile getImageFile() {
    return getParseFile(IMAGE);
  }

  public Venue setImageFile(ParseFile file) {
    return builderPut(IMAGE, file);
  }

  public int getColor() {
    return getInt(COLOR);
  }

  public Venue setColor(int color) {
    return builderPut(COLOR, color);
  }

  public LatLng getLocation() {
    ParseGeoPoint point = getParseGeoPoint(LOCATION);
    return new LatLng(point.getLatitude(), point.getLongitude());
  }

  public Venue setLocation(ParseGeoPoint location) {
    return builderPut(LOCATION, location);
  }

  public LatLngBounds getBounds() {
    List<ParseGeoPoint> points = getList(BOUNDS);
    LatLngBounds.Builder builder = LatLngBounds.builder();
    for (ParseGeoPoint point : points) {
      builder.include(new LatLng(point.getLatitude(), point.getLongitude()));
    }
    return builder.build();
  }

  public List<LatLng> getGeometry() {
    return Lists.transform(this.<ParseGeoPoint>getList(BOUNDS), new Function<ParseGeoPoint, LatLng>() {
      @Override
      public LatLng apply(ParseGeoPoint input) {
        return new LatLng(input.getLatitude(), input.getLongitude());
      }
    });
  }

  public Venue setGeometry(List<ParseGeoPoint> bounds) {
    return builderPut(BOUNDS, bounds);
  }

  public Geofence toGeofence() {
    LatLngBounds bounds = getBounds();
    LatLng center = bounds.getCenter();
    double radius = SphericalUtil.computeDistanceBetween(bounds.northeast, bounds.southwest) / 2;
    return new Geofence.Builder()
      .setCircularRegion(center.latitude, center.longitude, (float) radius)
      .setRequestId(getObjectId())
      .setLoiteringDelay(LOITERING_DELAY)
      .setNotificationResponsiveness(NOTIFICATION_RESPONSIVENESS)
      .setTransitionTypes(
        Geofence.GEOFENCE_TRANSITION_ENTER |
        Geofence.GEOFENCE_TRANSITION_DWELL |
        Geofence.GEOFENCE_TRANSITION_EXIT)
      .setExpirationDuration(LOITERING_DELAY)
      .build();
  }

  public static ParseQuery<Venue> query() {
    return ParseQuery.getQuery(Venue.class).fromLocalDatastore();
  }

  public static ParseQuery<Venue> remoteQuery() {
    return ParseQuery.getQuery(Venue.class);
  }

  public static final Creator<Venue> CREATOR = new Creator<Venue>() {
    @Override
    public Venue createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Venue[] newArray(int i) {
      return new Venue[0];
    }
  };

  public static Synchronize<Venue> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<Venue>() {
      @Override
      public ParseQuery<Venue> create() {
        return remoteQuery();
      }
    });
  }

}
