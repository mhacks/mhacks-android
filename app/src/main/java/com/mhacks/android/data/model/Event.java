package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable {

    private static final String TAG = "Event";

    public static final String CATEGORY_COL   = "category";
    public static final String DETAILS_COL    = "details";
    public static final String DURATION_COL   = "duration";
    public static final String HOST_COL       = "host";
    public static final String LOCATIONS_COL  = "locations";
    public static final String START_TIME_COL = "startTime";
    public static final String TITLE_COL      = "title";

    public Event() {}

    public EventType getCategory() {
        return (EventType) getParseObject(CATEGORY_COL);
    }

    public void setCategory(EventType category) {
        put(CATEGORY_COL, category);
    }

    public String getDetails() {
        return getString(DETAILS_COL);
    }

    public void setDetails(String details) {
        put(DETAILS_COL, details);
    }

    public int getDuration() {
        return getInt(DURATION_COL);
    }

    public void setDuration(int duration) {
        put(DURATION_COL, duration);
    }

    public Sponsor getHost() {
        return (Sponsor) getParseObject(HOST_COL);
    }

    public void setHost(Sponsor sponsor) {
        put(HOST_COL, sponsor);
    }

    public JSONArray getLocations() {
        return getJSONArray(LOCATIONS_COL);
    }

    public void setLocations(JSONArray locations) {
        put(LOCATIONS_COL, locations);
    }

    public Date getStartTime() {
        return getDate(START_TIME_COL);
    }

    public void setStartTime(Date date) {
        put(START_TIME_COL, date);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeParcelable(getCategory(), i);
        parcel.writeString(getDetails());
        parcel.writeInt(getDuration());
        parcel.writeParcelable(getHost(), i);
        parcel.writeString(getLocations().toString()); //JSONArray to string.
        parcel.writeValue(getStartTime());
        parcel.writeString(getTitle());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel source) {
        try {
            setObjectId(source.readString());
            setCategory((EventType) source.readParcelable(EventType.class.getClassLoader()));
            setDetails(source.readString());
            setDuration(source.readInt());
            setHost((Sponsor) source.readParcelable(Sponsor.class.getClassLoader()));
            setLocations(new JSONArray(source.readString()));
            setStartTime((Date) source.readValue(Date.class.getClassLoader()));
            setTitle(source.readString());
        }
        catch (JSONException e) {
            Log.e(TAG, "JSON done goofed", e);
        }
    }
}
