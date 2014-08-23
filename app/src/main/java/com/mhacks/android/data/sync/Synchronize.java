package com.mhacks.android.data.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.google.common.collect.Lists;
import com.mhacks.android.data.model.DataClass;
import com.mhacks.android.data.model.Trash;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class Synchronize<T extends ParseObject> {

  int corePoolSize = 60;
  int maximumPoolSize = 80;
  int keepAliveTime = 10;
  BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
  Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

  public static final String TAG = "Synchronize";

  public static final int TIMEOUT = 30000;

  private final ParseQueryAdapter.QueryFactory<T> mQueryFactory;
  private final String mClassName;

  public Synchronize(ParseQueryAdapter.QueryFactory<T> queryFactory) {
    mQueryFactory = queryFactory;
    ParseQuery<T> query = mQueryFactory.create();
    mClassName = query.getClassName();
    query.cancel();
  }

  public Date sync(Date since) throws SyncException {
    Log.d(TAG, "START: " + mClassName);

    try {

      // fetch new remote objects
      List<T> updatedRemoteObjects = getUpdatedRemoteObjects(since);

      // fetch deleted remote objects
      List<Trash> deletedRemoteObjects = getDeletedRemoteObjects(since);

      // Pin all the new remote objects
      for (T object : updatedRemoteObjects) {
        object.pin();
      }

      // Unpin and delete all the objects deleted remotely
      for (Trash trash : deletedRemoteObjects) {
        try {
          T object = get(trash.getDeletedObjectId());
          object.unpin();
        } catch (ParseException ignored) {
          ignored.printStackTrace();
        } // didn't have it, move on
      }

      // Return the latest modification date
      if (!updatedRemoteObjects.isEmpty() || !deletedRemoteObjects.isEmpty()) {
        List<ParseObject> allUpdated = Lists.<ParseObject>newArrayList(updatedRemoteObjects);
        allUpdated.addAll(deletedRemoteObjects);
        return Collections.max(allUpdated, UPDATED_AT).getUpdatedAt();
      }
      Log.d(TAG, "FINISH: " + mClassName);
      return since;

    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
      Log.e(TAG, "ERROR: " + mClassName);
      throw new SyncException(e);
    }

  }

  public Date sync(Date since, long timeout, TimeUnit timeUnit) throws SyncException {
    try {
      Task.Result result = new Task(since).executeOnExecutor(threadPoolExecutor).get(timeout, timeUnit);
      if (result.exception != null) throw result.exception;
      return result.since;
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new SyncException(e);
    }
  }

  public String getClassName() {
    return mClassName;
  }

  protected T get(String objectId) throws ParseException {
    return mQueryFactory.create().fromLocalDatastore().get(objectId);
  }

  protected List<T> getLocalObjects() throws ParseException {
    return mQueryFactory.create().fromLocalDatastore().find();
  }

  protected List<Trash> getDeletedRemoteObjects(Date since) throws ParseException {
    return Trash.deletedSince(mClassName, since).find();
  }

  protected List<T> getUpdatedRemoteObjects(Date since) throws ParseException {
    return mQueryFactory.create().whereGreaterThan(DataClass.UPDATED_AT, since).find();
  }

  public static class SyncException extends Exception {
    public SyncException() {
      super();
    }

    public SyncException(Throwable throwable) {
      super(throwable);
    }
  }

  public static final Comparator<ParseObject> UPDATED_AT = new Comparator<ParseObject>() {
    @Override
    public int compare(ParseObject first, ParseObject second) {
      return first.getUpdatedAt().compareTo(second.getUpdatedAt());
    }
  };

  private class Task extends AsyncTask<Void, Void, Task.Result> {
    private final Date mmDate;
    public Task(Date date) {
      mmDate = date;
    }

    @Override
    protected Result doInBackground(Void... voids) {
      Result result = new Result();
      try {
        result.since = sync(mmDate);
      } catch (SyncException e) {
        e.printStackTrace();
        result.exception = e;
      }
      return result;
    }

    public class Result {
      public SyncException exception = null;
      public Date since = null;
    }

  }

}