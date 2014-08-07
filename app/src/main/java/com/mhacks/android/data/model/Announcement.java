package com.mhacks.android.data.model;

import android.os.Parcel;

import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/26/14.
 */
@ParseClassName(Announcement.CLASS)
public class Announcement extends DataClass<Announcement> {
  public static final String CLASS = "Announcement";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String POSTER = "poster";
  public static final String PINNED = "pinned";
  public static final String PUSHED = "pushed";

  public Announcement() {
    super(false);
  }

  public Announcement(String title, String details, Sponsor poster, boolean pinned) {
    super(true);

    setTitle(title);
    setDetails(details);
    setPoster(poster);
    setPinned(pinned);
  }

  public static ParseQuery<Announcement> query() {
    return remoteQuery().fromLocalDatastore();
  }

  public static ParseQuery<Announcement> remoteQuery() {
    ParseQuery<Announcement> query = ParseQuery.getQuery(Announcement.class);
    query.include(POSTER);
    return query;
  }

  public String getTitle() {
    return getString(TITLE);
  }

  public Announcement setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public Announcement setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public boolean isPinned() {
    return getBoolean(PINNED);
  }

  public Announcement setPinned(boolean pinned) {
    return builderPut(PINNED, pinned);
  }

  public boolean isPushed() {
    return getBoolean(PUSHED);
  }

  public Announcement push() {
    JSONObject data = new JSONObject();
    try {
      data.put("foo", "bar");
      data.put("spam", "eggs");
      data.put("one", 1);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ParsePush.sendDataInBackground(data, ParseInstallation.getQuery());
    return this;
  }

  public Sponsor getPoster() {
    return (Sponsor) getParseObject(POSTER);
  }

  public Announcement setPoster(Sponsor poster) {
    return builderPut(POSTER, poster);
  }

  public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
    @Override
    public Announcement createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Announcement[] newArray(int i) {
      return new Announcement[0];
    }
  };

  public static Synchronize<Announcement> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<Announcement>() {
      @Override
      public ParseQuery<Announcement> create() {
        return remoteQuery();
      }
    });
  }

}
