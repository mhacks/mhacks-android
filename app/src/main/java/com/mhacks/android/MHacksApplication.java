package com.mhacks.android;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.CountdownItem;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.iv.android.R;
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

        /*New Data*/
        ParseObject.registerSubclass(CountdownItem.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                         getString(R.string.parse_application_id),
                         getString(R.string.parse_client_key));
        PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseFacebookUtils.initialize(getString(R.string.fb_app_id));
        ParseTwitterUtils.initialize(getString(R.string.twitter_key),
                                     getString(R.string.twitter_secret));

        Bugsnag.register(this, getString(R.string.bugsnag_key));
    }

    public static MHacksApplication getInstance() {

        return sApplication;
    }
}
