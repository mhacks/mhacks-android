package com.mhacks.android.data.model;

import android.os.Parcel;

import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(Sponsor.CLASS)
public class Sponsor extends DataClass<Sponsor> {
  public static final String CLASS = "Sponsor";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String URL = "url";
  public static final String LOGO = "logo";
  public static final String IMAGE = "image";
  public static final String COLOR = "color";
  public static final String ORDERING = "ordering";

  public Sponsor() {
    super(false);
  }

  public Sponsor(String title, String details, String url, ParseFile logo, int ordering) {
    super(true);

    setTitle(title);
    setDetails(details);
    setUrl(url);
    setLogoFile(logo);
    setOrdering(ordering);
  }

  public String getTitle() {
    return getString(TITLE);
  }

  public Sponsor setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public Sponsor setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public String getUrl() {
    return getString(URL);
  }

  public Sponsor setUrl(String url) {
    return builderPut(URL, url);
  }

  public ParseFile getLogoFile() {
    return getParseFile(LOGO);
  }

  public Sponsor setLogoFile(ParseFile logo) {
    return builderPut(LOGO, logo);
  }

  public ParseFile getImageFile() {
    return getParseFile(IMAGE);
  }

  public Sponsor setImageFile(ParseFile image) {
    return builderPut(IMAGE, image);
  }

  public int getColor() {
    return getInt(COLOR);
  }

  public Sponsor setColor(int color) {
    return builderPut(COLOR, color);
  }

  public int getOrdering() {
    return getInt(ORDERING);
  }

  public Sponsor setOrdering(int ordering) {
    return builderPut(ORDERING, ordering);
  }

  public static ParseQuery<Sponsor> query() {
    return remoteQuery().fromLocalDatastore();
  }

  public static ParseQuery<Sponsor> remoteQuery() {
    ParseQuery<Sponsor> query = ParseQuery.getQuery(Sponsor.class);
//    query.include(LOGO);
//    query.include(IMAGE);
    return query;
  }

  public static final Creator<Sponsor> CREATOR = new Creator<Sponsor>() {
    @Override
    public Sponsor createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Sponsor[] newArray(int i) {
      return new Sponsor[i];
    }
  };

  public static Synchronize<Sponsor> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<Sponsor>() {
      @Override
      public ParseQuery<Sponsor> create() {
        return remoteQuery();
      }
    });
  }

}
