package com.mhacks.android.data.sync;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Trash;
import com.mhacks.android.data.model.User;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/28/14.
 */
public class UserSynchronize extends Synchronize<User> {

  public UserSynchronize(ParseQueryAdapter.QueryFactory<User> queryFactory) {
    super(queryFactory);
  }

  @Override
  public Date sync(Date since) throws SyncException {

    try {

      // Fetch all pinned objects and their ids
      List<User> localObjects = getLocalObjects();

      // register unregistered users
      for (User user : localObjects) {
        if (!user.has(User.USERNAME)) user.signUp();
      }

      // fetch new remote objects
      List<User> updatedRemoteObjects = getUpdatedRemoteObjects(since);

      // fetch deleted remote objects
      List<Trash> deletedRemoteObjects = getDeletedRemoteObjects(since);

      // Pin all the new remote objects
      for (User user : updatedRemoteObjects) {
        user.pin();
      }

      // Unpin and delete all the objects deleted remotely
      for (Trash trash : deletedRemoteObjects) {
        try {
          User user = get(trash.getDeletedObjectId());
          user.unpin();
          user.delete();
        } catch (ParseException ignored) {
        } // didn't have it, move on
      }

      // Return the latest modification date
      if (!updatedRemoteObjects.isEmpty()) {
        return Collections.max(updatedRemoteObjects, UPDATED_AT).getUpdatedAt();
      }
      return since;

    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
      throw new SyncException(e);
    }

  }

}