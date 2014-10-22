package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("Annoucement")
public class Announcement extends ParseObject implements Parcelable {

    public static final String AUTHOR_COL  = "author";
    public static final String EVENT_COL   = "event";
    public static final String MESSAGE_COL = "message";
    public static final String TIME_COL    = "time";
    public static final String TITLE_COL   = "title";

    public Announcement() {}

    public Sponsor getAuthor() {
        return (Sponsor) getParseObject(AUTHOR_COL);
    }

    public void setAuthor(Sponsor sponsor) {
        put(AUTHOR_COL, sponsor);
    }

    public Event getEvent() {
        return (Event) getParseObject(EVENT_COL);
    }

    public void setEvent(Event event) {
        put(EVENT_COL, event);
    }

    public String getMessage() {
        return getString(MESSAGE_COL);
    }

    public void setMessage(String message) {
        put(MESSAGE_COL, message);
    }

    public Date getTime() {
        return getDate(TIME_COL);
    }

    public void setTime(Date time) {
        put(TIME_COL, time);
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
        parcel.writeParcelable(getAuthor(), i);
        parcel.writeParcelable(getEvent(), i);
        parcel.writeString(getMessage());
        parcel.writeValue(getTime());
        parcel.writeString(getTitle());
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel source) {
            return new Announcement(source);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };

    private Announcement(Parcel source) {
        setObjectId(source.readString());
        setAuthor((Sponsor) source.readParcelable(Sponsor.class.getClassLoader()));
        setEvent((Event) source.readParcelable(Event.class.getClassLoader()));
        setMessage(source.readString());
        setTime((Date) source.readValue(Date.class.getClassLoader()));
        setTitle(source.readString());
    }
}
