package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("CountdownItem")
public class CountdownItem extends ParseObject implements Parcelable {

    public static final String TIME_COL  = "time";
    public static final String TITLE_COL = "title";

    public CountdownItem() {}

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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getObjectId());
        parcel.writeValue(getTime());
        parcel.writeString(getTitle());
    }

    public static final Creator<CountdownItem> CREATOR = new Creator<CountdownItem>() {
        @Override
        public CountdownItem createFromParcel(Parcel source) {
            return new CountdownItem(source);
        }

        @Override
        public CountdownItem[] newArray(int size) {
            return new CountdownItem[size];
        }
    };

    private CountdownItem(Parcel source) {
        setObjectId(source.readString());
        setTime((Date) source.readValue(Date.class.getClassLoader()));
        setTitle(source.readString());
    }


}
