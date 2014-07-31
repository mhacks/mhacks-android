package com.mhacks.android.data.sync;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.bugsnag.android.Bugsnag;
import com.google.common.base.Optional;
import com.mhacks.android.data.model.User;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class Synchronization extends AsyncTask<Void, Void, Void> {

  private static boolean sSyncing = false;

  private Optional<SyncCallbacks> mCallbacks = Optional.absent();
  private Optional<SwipeRefreshLayout> mLayout = Optional.absent();

  public Synchronization() {
  }

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
  protected Void doInBackground(Void... voids) {
    if (sSyncing) return null;
    sSyncing = true;
    if (mCallbacks.isPresent()) mCallbacks.get().onSyncStarted();
    if (mLayout.isPresent()) mLayout.get().setRefreshing(true);

    try {
      User.getSync().sync();
    } catch (Synchronize.SyncException e) {
      return error(e);
    }

    return finish();
  }

  protected Void error(Synchronize.SyncException e) {
    e.printStackTrace();
    Bugsnag.notify(e);
    sSyncing = false;
    if (mCallbacks.isPresent()) mCallbacks.get().onSyncError(e);
    if (mLayout.isPresent()) mLayout.get().setRefreshing(false);
    return null;
  }

  protected Void finish() {
    sSyncing = false;
    if (mCallbacks.isPresent()) mCallbacks.get().onSyncCompleted();
    if (mLayout.isPresent()) mLayout.get().setRefreshing(false);
    return null;
  }

  public static interface SyncCallbacks {
    public void onSyncStarted();

    public void onSyncCompleted();

    public void onSyncError(Synchronize.SyncException e);
  }

}

