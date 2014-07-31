package com.mhacks.android;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.User;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class MHacksApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ParseObject.registerSubclass(User.class);

    Parse.enableLocalDatastore(this);
    Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));

    Bugsnag.register(this, getString(R.string.bugsnag_key));
  }
}
