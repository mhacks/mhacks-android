package com.mhacks.android.data.sync;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.bugsnag.android.Bugsnag;
import com.google.common.base.Optional;
import com.mhacks.android.MHacksApplication;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.model.Venue;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRole;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class Synchronization extends AsyncTask<Void, Void, Synchronization.SyncResult> {

  public static final String LAST_SYNC = "_synchronization_last_sync:";

  private static boolean sSyncing = false;

  private Optional<SyncCallbacks> mCallbacks = Optional.absent();
  private Optional<SwipeRefreshLayout> mLayout = Optional.absent();

  private SharedPreferences mPreferences;
  private List<Synchronize<? extends ParseObject>> mSyncs;
  private Map<String, Date> mSince;

  public Synchronization() {}

  public Synchronization(SyncCallbacks callbacks) {
    mCallbacks = Optional.fromNullable(callbacks);
  }

  public Synchronization(SwipeRefreshLayout layout) {
    mLayout = Optional.fromNullable(layout);
  }

  public Synchronization(SyncCallbacks callbacks, SwipeRefreshLayout layout) {
    mCallbacks = Optional.fromNullable(callbacks);
    mLayout = Optional.fromNullable(layout);
  }

  @Override
  protected void onPreExecute() {
    if (mCallbacks.isPresent()) mCallbacks.get().onSyncStarted();
    if (mLayout.isPresent()) mLayout.get().setRefreshing(true);

    Synchronize<ParseRole> roleSync = new Synchronize<>(new ParseQueryAdapter.QueryFactory<ParseRole>() {
      @Override
      public ParseQuery<ParseRole> create() {
        return ParseRole.getQuery();
      }
    });

    mSyncs = Arrays.asList(
      Announcement.getSync(),
      Award.getSync(),
      Event.getSync(),
      Venue.getSync(),
      Sponsor.getSync(),
      User.getSync(),
      roleSync
    );

    mPreferences = PreferenceManager.getDefaultSharedPreferences(MHacksApplication.getInstance());
    mSince = new HashMap<>();
    for (Synchronize<? extends ParseObject> sync : mSyncs) {
      Date since = new Date(mPreferences.getLong(LAST_SYNC + sync.getClassName(), 0));
      mSince.put(sync.getClassName(), since);
    }
  }

  @Override
  protected SyncResult doInBackground(Void... voids) {
    if (sSyncing) return new SyncResult(null, null);
    sSyncing = true;

    try {

      Map<String, Date> dates = new HashMap<>();
      for (Synchronize<? extends ParseObject> sync : mSyncs) {
        dates.put(sync.getClassName(), sync.sync(mSince.get(sync.getClassName())));
      }

      return new SyncResult(null, dates);

    } catch (Synchronize.SyncException e) {
      return new SyncResult(e, null);
    }

  }

  @Override
  protected void onPostExecute(SyncResult result) {
    if (result.exception == null) {
      if (mCallbacks.isPresent()) mCallbacks.get().onSyncCompleted();
      if (mLayout.isPresent()) mLayout.get().setRefreshing(false);
    }
    else {
      result.exception.printStackTrace();
      Bugsnag.notify(result.exception);
      if (mCallbacks.isPresent()) mCallbacks.get().onSyncError(result.exception);
      if (mLayout.isPresent()) mLayout.get().setRefreshing(false);
    }

    if (result.dates != null) {
      SharedPreferences.Editor editor = mPreferences.edit();
      for (Map.Entry<String, Date> entry : result.dates.entrySet()) {
        editor.putLong(LAST_SYNC + entry.getKey(), entry.getValue().getTime());
      }
      editor.commit();
      sSyncing = false;
    }
  }

  public static final class SyncResult {
    public final Synchronize.SyncException exception;
    public final Map<String, Date> dates;

    public SyncResult(Synchronize.SyncException exception, Map<String, Date> dates) {
      this.exception = exception;
      this.dates = dates;
    }
  }

  public static interface SyncCallbacks {
    public void onSyncStarted();

    public void onSyncCompleted();

    public void onSyncError(Synchronize.SyncException e);
  }

}

