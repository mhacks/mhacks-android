package com.mhacks.android;

import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.CountdownItem;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.EventType;
import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.SponsorTier;
import com.mhacks.iv.android.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

/**
 * Created by Omkar Moghe on 11/15/2014.
 */
public class MHacks extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Announcement.class);
        ParseObject.registerSubclass(Award.class);
        ParseObject.registerSubclass(CountdownItem.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(EventType.class);
        ParseObject.registerSubclass(Location.class);
        ParseObject.registerSubclass(Sponsor.class);
        ParseObject.registerSubclass(SponsorTier.class);


        // enabling local data store causes weird 'ParseObject not found for update' error
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        PushService.startServiceIfRequired(getApplicationContext());

        Bugsnag.register(this, getString(R.string.bugsnag_key));
    }

}
