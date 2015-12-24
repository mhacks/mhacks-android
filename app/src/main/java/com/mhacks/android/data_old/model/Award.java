package com.mhacks.android.data_old.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("Award")
public class Award extends ParseObject implements Parcelable {

    public static final String DESCRIPTION_COL = "description";
    public static final String PRIZE_COL       = "prize";
    public static final String SPONSOR_COL     = "sponsor";
    public static final String TITLE_COL       = "title";
    public static final String VALUE_COL       = "value";
    public static final String WEBSITE_COL     = "website";

    public Award() {}

    public String getDescription() {
        return getString(DESCRIPTION_COL);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_COL, description);
    }

    public String getPrize() {
        return getString(PRIZE_COL);
    }

    public void setPrize(String prize) {
        put(PRIZE_COL, prize);
    }

    public Sponsor getSponsor() {
        return (Sponsor) getParseObject(SPONSOR_COL);
    }

    public void setSponsor(Sponsor sponsor) {
        put(SPONSOR_COL, sponsor);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    public int getValue() {
        return getInt(VALUE_COL);
    }

    public void setValue(int value) {
        put(VALUE_COL, value);
    }

    public String getWebsite() {
        return getString(WEBSITE_COL);
    }

    public void setWebsite(String website) {
        put(WEBSITE_COL, website);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getDescription());
        parcel.writeString(getPrize());
        parcel.writeParcelable(getSponsor(), i);
        parcel.writeString(getTitle());
        parcel.writeInt(getValue());
        parcel.writeString(getWebsite());
    }

    public static final Creator<Award> CREATOR = new Creator<Award>() {
        @Override
        public Award createFromParcel(Parcel source) {
            return new Award(source);
        }

        @Override
        public Award[] newArray(int size) {
            return new Award[size];
        }
    };

    private Award(Parcel source) {
        setObjectId(source.readString());
        setDescription(source.readString());
        setPrize(source.readString());
        setSponsor((Sponsor) source.readParcelable(Sponsor.class.getClassLoader()));
        setTitle(source.readString());
        setValue(source.readInt());
        setWebsite(source.readString());
    }
}
