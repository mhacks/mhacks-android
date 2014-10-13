package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omkar Moghe on 10/13/2014.
 */
@ParseClassName("EventType")
public class EventType extends ParseObject implements Parcelable{

    public static final String COLOR_COL = "color";
    public static final String TITLE_COL = "title";

    public String getColor() {
        return getString(COLOR_COL);
    }

    public void setColor(String color) {
        put(COLOR_COL, color);
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

    }
}
