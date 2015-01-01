package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Omkar Moghe on 12/31/2014.
 */
@ParseClassName("Map")
public class Map extends ParseObject implements Parcelable{

    public static final String TAG = "Map";

    public static final String ORDER_COL   = "order";
    public static final String TITLE_COL   = "title";
    public static final String IMAGE_COL   = "image";

    public int getOrder () {
        return getInt(ORDER_COL);
    }

    public void setOrder (int order) {
        put(ORDER_COL, order);
    }

    public String getTitle () {
        return getString(TITLE_COL);
    }

    public void setTitle (String title) {
        put(TITLE_COL, title);
    }

    public ParseFile getImage () {
        return getParseFile(IMAGE_COL);
    }

    public void setImage (ParseFile image) {
        put(IMAGE_COL, image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getTitle());
        parcel.writeInt(getOrder());
        parcel.writeValue(getImage());
    }

    public static final Creator<Map> CREATOR = new Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel source) {
            return new Map(source);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };

    private Map(Parcel source) {
        setObjectId(source.readString());
        setTitle(source.readString());
        setOrder(source.readInt());
        setImage((ParseFile) source.readValue(ParseFile.class.getClassLoader()));
    }
}
