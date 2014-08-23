package com.mhacks.android.data.model;

import android.os.Parcel;

import com.parse.ParseClassName;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(Trash.CLASS)
public class Trash extends DataClass<Trash> {
  public static final String CLASS = "Trash";

  public static final String DELETED_OBJECT_ID = "deletedObjectId";
  public static final String CLASS_NAME = "class";

  public Trash() {
    super(false);
  }

  public String getDeletedObjectId() {
    return getString(DELETED_OBJECT_ID);
  }

  public static ParseQuery<Trash> deletedSince(String className, Date since) {
    return ParseQuery.getQuery(Trash.class)
      .whereGreaterThan(UPDATED_AT, since)
      .whereEqualTo(CLASS_NAME, className);
  }

  public static final Creator<Trash> CREATOR = new Creator<Trash>() {
    @Override
    public Trash createFromParcel(Parcel parcel) {
      return null;
    }

    @Override
    public Trash[] newArray(int i) {
      return new Trash[0];
    }
  };

}
