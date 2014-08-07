package com.mhacks.android.data.model;


import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/26/14.
 */
@ParseClassName(Installation.CLASS)
public class Installation extends ParseInstallation {
  public static final String CLASS = "_Installation";

  public static final String APP_IDENTIFIER = "appIdentifier";
  public static final String APP_NAME = "appName";
  public static final String APP_VERSION = "appVersion";
  public static final String USER = "user";
  public static final String BADGE = "badge";
  public static final String CHANNELS = "channels";
  public static final String DEVICE_TOKEN = "deviceToken";
  public static final String DEVICE_TOKEN_LAST_MODIFIED = "deviceTokenLastModified";
  public static final String DEVICE_TYPE = "deviceType";
  public static final String INSTALLATION_ID = "installationId";
  public static final String PARSE_VERSION = "parseVersion";
  public static final String PUSH_TYPE = "pushType";
  public static final String TIME_ZONE = "timeZone";

  public Installation() {
    super();
  }

  public static Installation getCurrentInstallation() {
    return (Installation) ParseInstallation.getCurrentInstallation();
  }

  public static ParseQuery<Installation> query() {
    return remoteQuery();
  }

  public static ParseQuery<Installation> remoteQuery() {
    return ParseQuery.getQuery(Installation.class);
  }

  public User getUser() {
    return (User) getParseUser(USER);
  }

  public Installation setUser(User user) {
    if (user != null) put(USER, user);
    return this;
  }

  public Installation setCurrentUser() {
    return setUser(User.getCurrentUser());
  }

  public String getAppIdentifier() {
    return getString(APP_IDENTIFIER);
  }

  public String getAppName() {
    return getString(APP_NAME);
  }

  public String getAppVersion() {
    return getString(APP_VERSION);
  }

  public int getBadge() {
    return getInt(BADGE);
  }

  public List<String> getChannels() {
    return getList(CHANNELS);
  }

  public Installation subscribe(String channel) {
    add(CHANNELS, channel);
    return this;
  }

  public Installation unSubscribe(String channel) {
    removeAll(CHANNELS, Arrays.asList(channel));
    return this;
  }

  public String getDeviceToken() {
    return getString(DEVICE_TOKEN);
  }

  public long getDeviceTokenLastModified() {
    return getLong(DEVICE_TOKEN_LAST_MODIFIED);
  }

  public String getDeviceType() {
    return getString(DEVICE_TYPE);
  }

  public String getInstallationId() {
    return getString(INSTALLATION_ID);
  }

  public String getParseVersion() {
    return getString(PARSE_VERSION);
  }

  public String getPushType() {
    return getString(PUSH_TYPE);
  }

  public String getTimeZone() {
    return getString(TIME_ZONE);
  }

  @Override
  public void saveEventually(SaveCallback callback) {
    if (!has(USER)) setCurrentUser();
    super.saveEventually(callback);
  }
}
