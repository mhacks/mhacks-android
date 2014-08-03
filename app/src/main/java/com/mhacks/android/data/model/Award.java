package com.mhacks.android.data.model;

import android.os.Parcel;

import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(Award.CLASS)
public class Award extends DataClass<Award> {
  public static final String CLASS = "Award";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String VALUE = "value";
  public static final String SPONSOR = "sponsor";
  public static final String PRIZE = "prize";

  public Award() {
    super(false);
  }

  public Award(String title, String details, int value, Sponsor sponsor, String prize) {
    super(true);

    setTitle(title);
    setDetails(details);
    setValue(value);
    setSponsor(sponsor);
    setPrize(prize);
  }

  public String getTitle() {
    return getString(TITLE);
  }

  public Award setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public Award setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public int getValue() {
    return getInt(VALUE);
  }

  public Award setValue(int value) {
    return builderPut(VALUE, value);
  }

  public Sponsor getSponsor() {
    return (Sponsor) getParseObject(SPONSOR);
  }

  public Award setSponsor(Sponsor sponsor) {
    return builderPut(SPONSOR, sponsor);
  }

  public String getPrize() {
    return getString(PRIZE);
  }

  public Award setPrize(String prize) {
    return builderPut(PRIZE, prize);
  }

  public static ParseQuery<Award> query() {
    return remoteQuery().fromLocalDatastore();
  }

  public static ParseQuery<Award> remoteQuery() {
    ParseQuery<Award> query = ParseQuery.getQuery(Award.class);
    query.include(SPONSOR);
    return query;
  }

  public static final Creator<Award> CREATOR = new Creator<Award>() {
    @Override
    public Award createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Award[] newArray(int i) {
      return new Award[0];
    }
  };

  public static Synchronize<Award> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<Award>() {
      @Override
      public ParseQuery<Award> create() {
        return remoteQuery();
      }
    });
  }

}
