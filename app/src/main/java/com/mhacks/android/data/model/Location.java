package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omkar on 10/13/2014.
 */
@ParseClassName("Location")
public class Location extends ParseObject implements Parcelable{

    public static final String NAME_COL = "name";

    public String getName() {
        return getString(NAME_COL);
    }

    public void setName(String name) {
        put(NAME_COL, name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
