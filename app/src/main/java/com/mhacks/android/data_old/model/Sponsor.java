package com.mhacks.android.data_old.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Omkar Moghe on 10/13/2014.
 */
@ParseClassName("Sponsor")
public class Sponsor extends ParseObject implements Parcelable {

    private static final String TAG = "Sponsor";

    public static final String DESCRIPTION_COL = "description";
    public static final String LOCATION_COL    = "location";
    public static final String LOGO_COL        = "logo";
    public static final String NAME_COL        = "name";
    public static final String TIER_COL        = "tier";
    public static final String WEBSITE_COL     = "website";

    public Sponsor() {}

    public String getDescription() {
        return getString(DESCRIPTION_COL);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_COL, description);
    }

    public Location getLocation() {
        return (Location) getParseObject(LOCATION_COL);
    }

    public void setLocation(Location location) {
        put(LOCATION_COL, location);
    }

    public ParseFile getLogo() {
        return getParseFile(LOGO_COL);
    }

    public void setLogo(ParseFile parseFile) {
        put(LOGO_COL, parseFile);
    }

    public String getName() {
        return getString(NAME_COL);
    }

    public void setName(String name) {
        put(NAME_COL, name);
    }

    public SponsorTier getTier() {
        return (SponsorTier) getParseObject(TIER_COL);
    }

    public void setTier(SponsorTier sponsorTier) {
        put(TIER_COL, sponsorTier);
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
        parcel.writeParcelable(getLocation(), i);
        parcel.writeString(getName());
        parcel.writeParcelable(getTier(), i);
        parcel.writeString(getWebsite());
        parcel.writeValue(getLogo());
    }

    public static final Creator<Sponsor> CREATOR = new Creator<Sponsor>() {
        @Override
        public Sponsor createFromParcel(Parcel source) {
            return new Sponsor(source);
        }

        @Override
        public Sponsor[] newArray(int size) {
            return new Sponsor[size];
        }
    };

    private Sponsor(Parcel source) {
            setObjectId(source.readString());
            setDescription(source.readString());
            setLocation((Location) source.readParcelable(Location.class.getClassLoader()));
            setName(source.readString());
            setTier((SponsorTier) source.readParcelable(SponsorTier.class.getClassLoader()));
            setWebsite(source.readString());
            setLogo((ParseFile) source.readValue(ParseFile.class.getClassLoader()));
    }
}
