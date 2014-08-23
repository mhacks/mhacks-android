package com.mhacks.android.data.model;

import android.os.Parcel;

import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(Event.CLASS)
public class Event extends DataClass<Event> {
  public static final String CLASS = "Event";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String IMAGE = "image";
  public static final String COLOR = "color";
  public static final String HOST = "host";
  public static final String TIME = "time";
  public static final String LOCATION = "location";

  public Event() {
    super(false);
  }

  public Event(String title, String details, Sponsor host, Date time, Venue location) {
    super(true);

    setTitle(title);
    setDetails(details);
    setHost(host);
    setTime(time);
    setLocation(location);
  }

  public String getTitle() {
    return getString(TITLE);
  }
  public Event setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public Event setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public ParseFile getImageFile() {
    return getParseFile(IMAGE);
  }

  public Event setImageFile(ParseFile file) {
    return builderPut(IMAGE, file);
  }

  public int getColor() {
    return getInt(COLOR);
  }

  public Event setColor(int color) {
    return builderPut(COLOR, color);
  }

  public Sponsor getHost() {
    return (Sponsor) getParseObject(HOST);
  }

  public Event setHost(Sponsor sponsor) {
    return builderPut(HOST, sponsor);
  }

  public Date getTime() {
    return getDate(TIME);
  }

  public Event setTime(Date time) {
    return builderPut(TIME, time);
  }

  public Venue getLocation() {
    return (Venue) getParseObject(LOCATION);
  }

  public Event setLocation(Venue location) {
    return builderPut(LOCATION, location);
  }
  
  public static ParseQuery<Event> query() {
    ParseQuery<Event> query = remoteQuery().fromLocalDatastore();
    query.include(IMAGE);
    query.include(HOST);
    query.include(LOCATION);
    return query;
  }

  public static ParseQuery<Event> remoteQuery() {
    return ParseQuery.getQuery(Event.class);
  }

  public static final Creator<Event> CREATOR = new Creator<Event>() {
    @Override
    public Event createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Event[] newArray(int i) {
      return new Event[0];
    }
  };

  public static Synchronize<Event> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<Event>() {
      @Override
      public ParseQuery<Event> create() {
        return remoteQuery();
      }
    });
  }

}
