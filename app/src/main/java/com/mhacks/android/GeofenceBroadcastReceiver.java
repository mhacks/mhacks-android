package com.mhacks.android;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.model.Venue;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/8/14.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
  public static final String TAG = "GeofenceBroadcastReceiver";

  public static GeofenceInitializer initialize(Context context) {
    return new GeofenceInitializer(context);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if(LocationClient.hasError(intent)){
      int errorCode = LocationClient.getErrorCode(intent);
      Log.e(TAG, "Location Services error: " + Integer.toString(errorCode));
      Bugsnag.notify(new Exception(Integer.toString(errorCode)));
      return;
    }

    List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
    if (geofences.isEmpty()) return;
    try {
      User.updateVenue(LocationClient.getGeofenceTransition(intent) == Geofence.GEOFENCE_TRANSITION_EXIT ? null : geofences.get(0).getRequestId());
    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
    }
  }

  private static class GeofenceInitializer implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener {

    private Context mmContext;
    private LocationClient mmLocationClient;

    private GeofenceInitializer(Context context) {
      mmContext = context;
      mmLocationClient = new LocationClient(mmContext, this, this);
      mmLocationClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
      try {
        List<Geofence> geofences = getGeofences();
        if (geofences.isEmpty()) {
          mmLocationClient.disconnect();
          return;
        }
        Intent intent = new Intent(mmContext, GeofenceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mmContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mmLocationClient.addGeofences(geofences, pendingIntent, this);
      } catch (ParseException e) {
        Log.e(TAG, "Failed to add geofences");
        e.printStackTrace();
        mmLocationClient.disconnect();
      }
    }

    @Override
    public void onDisconnected() {
      mmLocationClient = null;
      mmContext = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      onDisconnected();
    }

    public List<Geofence> getGeofences() throws ParseException {
      return Lists.transform(Venue.query().find(), new Function<Venue, Geofence>() {
        @Override
        public Geofence apply(Venue input) {
          return input.toGeofence();
        }
      });
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
      if (i != LocationStatusCodes.SUCCESS) Log.e(TAG, "Failed to add geofences");
      else Log.d(TAG, "Geofences successfully added");
      mmLocationClient.disconnect();
    }
  }

}
