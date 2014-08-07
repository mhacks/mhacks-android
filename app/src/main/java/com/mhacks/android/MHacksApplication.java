package com.mhacks.android;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Installation;
import com.mhacks.android.data.model.MapLocation;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class MHacksApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ParseObject.registerSubclass(Announcement.class);
    ParseObject.registerSubclass(Award.class);
    ParseObject.registerSubclass(Event.class);
    ParseObject.registerSubclass(Installation.class);
    ParseObject.registerSubclass(MapLocation.class);
    ParseObject.registerSubclass(Sponsor.class);
    ParseObject.registerSubclass(User.class);

    Parse.enableLocalDatastore(this);
    Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
    PushService.setDefaultPushCallback(this, MainActivity.class);

    Bugsnag.register(this, getString(R.string.bugsnag_key));
  }
}
