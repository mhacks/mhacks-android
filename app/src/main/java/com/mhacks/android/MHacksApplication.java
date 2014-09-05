package com.mhacks.android;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Installation;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.Trash;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.model.Venue;
import com.mhacks.android.ui.MainActivity;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.PushService;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class MHacksApplication extends Application {

  private static MHacksApplication sApplication;

  @Override
  public void onCreate() {
    super.onCreate();

    sApplication = this;

    ParseObject.registerSubclass(Announcement.class);
    ParseObject.registerSubclass(Award.class);
    ParseObject.registerSubclass(Event.class);
    ParseObject.registerSubclass(Installation.class);
    ParseObject.registerSubclass(Sponsor.class);
    ParseObject.registerSubclass(User.class);
    ParseObject.registerSubclass(Venue.class);
    ParseObject.registerSubclass(Trash.class);

    Parse.enableLocalDatastore(this);
    Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
    PushService.setDefaultPushCallback(this, MainActivity.class);

    ParseFacebookUtils.initialize(getString(R.string.fb_app_id));
    ParseTwitterUtils.initialize(getString(R.string.twitter_key), getString(R.string.twitter_secret));

    Bugsnag.register(this, getString(R.string.bugsnag_key));
  }

  public static MHacksApplication getInstance() {
    return sApplication;
  }
}
