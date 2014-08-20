package com.mhacks.android.data.sync;

import com.bugsnag.android.Bugsnag;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.mhacks.android.data.model.DataClass;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class Synchronize<T extends ParseObject> {
  private static final int SYNC_TIMEOUT = 1;
  private static final TimeUnit SYNC_TIMEOUT_UNIT = TimeUnit.MINUTES;

  private ParseQueryAdapter.QueryFactory<T> mQueryFactory;

  public Synchronize(ParseQueryAdapter.QueryFactory<T> queryFactory) {
    mQueryFactory = queryFactory;
  }

  public void sync() throws SyncException {

    try {
      // Fetch all pinned objects and their ids
      Map<String, T> localObjects = getLocalObjects();

      // fetch all remote objects and their ids
      Map<String, T> remoteObjects = getRemoteObjects();

      MapDifference<String, T> difference = Maps.difference(remoteObjects, localObjects, DataClass.equivalentOn(DataClass.OBJECT_ID));

      // Pin all the new remote objects
      for (T object : remoteObjects.values()) {
        object.pin();
      }

      // Unpin and delete all the objects deleted remotely
      for (T object : difference.entriesOnlyOnRight().values()) {
        object.unpin();
        object.delete();
      }

    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
      throw new SyncException(e);
    }

  }

  protected Map<String, T> getLocalObjects() throws ParseException {
    return DataClass.mapping(mQueryFactory.create().fromLocalDatastore().find());
  }

  protected Map<String, T> getRemoteObjects() throws ParseException {
    return DataClass.mapping(mQueryFactory.create().find());
  }

  public static class SyncException extends Exception {
    public SyncException() {
      super();
    }

    public SyncException(Throwable throwable) {
      super(throwable);
    }
  }

}