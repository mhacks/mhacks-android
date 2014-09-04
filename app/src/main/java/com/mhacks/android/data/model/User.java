package com.mhacks.android.data.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.sync.Synchronize;
import com.mhacks.android.data.sync.UserSynchronize;
import com.mhacks.android.ui.common.Util;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRole;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.twitter.Twitter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.UUID;

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
  public static final String NAME = "name";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String EMAIL_VERIFIED = "emailVerified";
  public static final String BIO = "bio";
  public static final String MAJOR = "major";
  public static final String SELFIE = "selfie";
  public static final String PHONE = "phone";
  public static final String SEX = "sex";
  public static final String CURRENT_VENUE = "currentVenue";
  public static final String SPONSOR = "sponsor";
  public static final String POSITION = "position";
  public static final String SPECIALTY = "specialty";
  public static final String SCHOOL = "school";
  public static final String AUTH_DATA = "authData";
  public static final String TWITTER_IMAGE_URL = "twitterImageUrl";

  public static final String FACEBOOK_URL = "https://graph.facebook.com/%s/picture?type=square";
  public static final String FACEBOOK = "facebook";
  public static final String TWITTER = "twitter";
  public static final String ID = "id";

  private Boolean mAdmin = null;

  public static String getFacebookImageUrl(String id) {
    return String.format(FACEBOOK_URL, id);
  }

  public User() {
    super();
  }

  public static User getCurrentUser() {
    return (User) ParseUser.getCurrentUser();
  }

  public static ParseQuery<User> query() {
    ParseQuery<User> result = remoteQuery().fromLocalDatastore();
    result.include(SPONSOR);
    return result;
  }

  public static ParseQuery<User> remoteQuery() {
    return ParseQuery.getQuery(User.class);
  }

  public static void updateVenue(String venueObjectId) throws ParseException {
    User user = getCurrentUser();
    if (user == null) return;
    if (venueObjectId == null) user.remove(CURRENT_VENUE);
    else user.put(CURRENT_VENUE, Venue.query().get(venueObjectId));
    user.saveEventually();
  }

  public static boolean canAdmin() {
    return getCurrentUser() != null && getCurrentUser().isAdmin();
  }

  public boolean isAdmin() {
    if (mAdmin != null) return mAdmin;
    try {
      ParseRole role = ParseRole.getQuery().fromLocalDatastore()
        .whereEqualTo(ROLE_NAME, ADMINISTRATOR).getFirst();
      mAdmin = role.getRelation(ROLE_USERS).getQuery()
        .whereEqualTo(OBJECT_ID, this.getObjectId()).count() == 1;

      return mAdmin;
    } catch (ParseException e) {
      e.printStackTrace();
      Bugsnag.notify(e);
    }
    return false;
  }

  public String getName() {
    return getString(NAME);
  }

  public User setName(String name) {
    put(NAME, name);
    return this;
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

  public boolean isSponsor() {
    return has(SPONSOR);
  }

  public Sponsor getSponsor() {
    return isSponsor() ? (Sponsor) getParseObject(SPONSOR) : null;
  }

  public User setSponsor(Sponsor sponsor) {
    put(SPONSOR, sponsor);
    return this;
  }

  public String getSpecialty() {
    return has(SPECIALTY) ? getString(SPECIALTY) : "";
  }

  public User setSpecialty(String specialty) {
    put(SPECIALTY, specialty);
    return this;
  }

  public String getSchool() {
    return getString(SCHOOL);
  }

  public User setSchool(String school) {
    put(SCHOOL, school);
    return this;
  }

  public String getImageUrl() {
    // Update the model with an image, if possible
    if (ParseFacebookUtils.isLinked(this)) {
      // I can't believe I have to use reflection to get the Facebook user ID.
      // WTF Parse.
      try {
        Field authDataField = ParseUser.class.getDeclaredField(AUTH_DATA);
        authDataField.setAccessible(true);
        JSONObject authData = (JSONObject) authDataField.get(this);
        String id = authData.getJSONObject(FACEBOOK).getString(ID);
        return getFacebookImageUrl(id);
      } catch (Exception e) {
        e.printStackTrace();
        Bugsnag.notify(e);
      }
    }
    else if (ParseTwitterUtils.isLinked(this)) {
      return getString(TWITTER_IMAGE_URL);
    }
    return null;
  }

  public class TwitterFetchTask extends AsyncTask<Void, Void, Exception> {

    @Override
    protected Exception doInBackground(Void... voids) {
      Twitter twitter = ParseTwitterUtils.getTwitter();
      String screenName = twitter.getScreenName();

      HttpClient client = new DefaultHttpClient();
      HttpGet verifyGet = new HttpGet("https://api.twitter.com/1.1/users/show.json?screen_name=" + screenName);
      twitter.signRequest(verifyGet);

      try {
        HttpResponse response = client.execute(verifyGet);
        JSONObject jsonObject = new JSONObject(Util.convertStreamToString(response.getEntity().getContent()));
        put(TWITTER_IMAGE_URL, jsonObject.getString("profile_image_url"));
        setName("@" + screenName);
        save();

      } catch (Exception e) {
        e.printStackTrace();
        Bugsnag.notify(e);
        return e;
      }

      return null;
    }
  }

  @Override
  public void signUp() throws ParseException {
    put(PASSWORD, UUID.randomUUID().toString());
    put(USERNAME, UUID.randomUUID().toString());
    super.signUp();
  }

  public void saveLater() {
    pinInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          e.printStackTrace();
          Bugsnag.notify(e);
          return;
        }
        saveEventually();
      }
    });
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
    return new UserSynchronize(new ParseQueryAdapter.QueryFactory<User>() {
      @Override
      public ParseQuery<User> create() {
        return remoteQuery().whereExists(SPONSOR);
      }
    });
  }

}
