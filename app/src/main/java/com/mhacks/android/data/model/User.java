package com.mhacks.android.data.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRole;
import com.parse.ParseUser;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/26/14.
 */
@ParseClassName(User.CLASS)
public class User extends ParseUser implements Parcelable {
  public static final String CLASS = "_User";

  public static final String ROLE_NAME = "name";
  public static final String ROLE_USERS = "users";
  public static final String ADMINISTRATOR = "Administrator";

  public static final String OBJECT_ID = "objectId";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String EMAIL_VERIFIED = "emailVerified";
  public static final String BIO = "bio";
  public static final String MAJOR = "major";
  public static final String SELFIE = "selfie";
  public static final String PHONE = "phone";
  public static final String SEX = "sex";

  private Boolean mAdmin = null;

  public User() {
    super();
  }

  public static User getCurrentUser() {
    return (User) ParseUser.getCurrentUser();
  }

  public static ParseQuery<User> query() {
    return remoteQuery();
  }

  public static ParseQuery<User> remoteQuery() {
    return ParseQuery.getQuery(User.class);
  }

  public boolean isAdmin() {
    if (mAdmin != null) return mAdmin;
    try {
      ParseRole role = ParseRole.getQuery().fromLocalDatastore()
        .whereEqualTo(ROLE_NAME, ADMINISTRATOR).getFirst();
      mAdmin = role.getRelation(ROLE_USERS).getQuery()
        .whereEqualTo(OBJECT_ID, this.getObjectId()).count() == 1;
    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
    }
    return false;
  }

  public String getFirstName() {
    return getString(FIRST_NAME);
  }

  public User setFirstName(String firstName) {
    put(FIRST_NAME, firstName);
    return this;
  }

  public String getLastName() {
    return getString(LAST_NAME);
  }

  public User setLastName(String lastName) {
    put(LAST_NAME, lastName);
    return this;
  }

  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  public boolean isEmailVerified() {
    return getBoolean(EMAIL_VERIFIED);
  }

  public User setEmailVerified(boolean verified) {
    put(EMAIL_VERIFIED, verified);
    return this;
  }

  public String getBio() {
    return getString(BIO);
  }

  public User setBio(String bio) {
    put(BIO, bio);
    return this;
  }

  public String getMajor() {
    return getString(MAJOR);
  }

  public User setMajor(String major) {
    put(MAJOR, major);
    return this;
  }

  public ParseFile getSelfieFile() {
    return getParseFile(SELFIE);
  }

  public Bitmap getSelfie() throws ParseException {
    byte[] data = getSelfieFile().getData();
    return BitmapFactory.decodeByteArray(data, 0, data.length);
  }

  public User setSelfie(ParseFile selfie) {
    put(SELFIE, selfie);
    return this;
  }

  public String getPhone() {
    return getString(PHONE);
  }

  public User setPhone(String phone) {
    put(PHONE, phone);
    return this;
  }

  public Sex getSex() {
    return getString(SEX).equals(Sex.FEMALE) ? Sex.F : Sex.M;
  }

  public enum Sex {
    M, F;

    private static final String MALE = "male";
    private static final String FEMALE = "female";
  }

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override
    public User createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
        Bugsnag.notify(e);
      }
      return null;
    }

    @Override
    public User[] newArray(int i) {
      return new User[0];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(getString(OBJECT_ID));
  }

  public static Synchronize<User> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<User>() {
      @Override
      public ParseQuery<User> create() {
        return remoteQuery();
      }
    });
  }

}
