package com.mhacks.android.data.sync;

import com.bugsnag.android.Bugsnag;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.mhacks.android.data.model.DataClass;
import com.mhacks.android.data.model.User;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;

import java.util.Map;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class UserSynchronize extends Synchronize<User> {

  public UserSynchronize(ParseQueryAdapter.QueryFactory<User> queryFactory) {
    super(queryFactory);
  }

  @Override
  public void sync() throws SyncException {

    try {
      // Fetch all pinned objects and their ids
      Map<String, User> localObjects = getLocalObjects();

      // fetch all remote objects and their ids
      Map<String, User> remoteObjects = getRemoteObjects();

      MapDifference<String, User> difference = Maps.difference(remoteObjects, localObjects, DataClass.equivalentOn(DataClass.OBJECT_ID));

      // register unregistered users
      for (User user : localObjects.values()) {
        if (!user.has(User.USERNAME)) user.signUp();
      }

      // Pin all the new remote objects
      for (User user : remoteObjects.values()) {
        user.pin();
      }

      // Unpin and delete all the objects deleted remotely
      for (User user : difference.entriesOnlyOnRight().values()) {
        user.unpin();
        user.delete();
      }

    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
      throw new SyncException(e);
    }

  }

}