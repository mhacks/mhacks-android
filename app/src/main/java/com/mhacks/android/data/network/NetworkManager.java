package com.mhacks.android.data.network;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mhacks.android.data.auth.Token;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.AnnouncementList;
import com.mhacks.android.data.model.Countdown;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.EventList;
import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.model.Map;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.network.deserializer.UserDeserializer;
import com.squareup.okhttp.Headers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by boztalay on 6/4/15.
 * Updated by omkarmoghe on 12/25/15 for the MHacks modular backend & Retrofit 2.0.0-beta2.
 *
 */
public class NetworkManager {

    private static final String TAG      = "NetworkManager";
    private static final String BASE_URL = "http://ec2-52-70-71-221.compute-1.amazonaws.com/v1/";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private HackathonNetworkService networkService;
    private Token                   mToken;
    private User                    currentUser;

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }

        return instance;
    }

    public NetworkManager() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserDeserializer())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat(DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.networkService = retrofit.create(HackathonNetworkService.class);
    }

    public void logUserIn(String email, String password, final HackathonCallback<User> callback) {
        networkService.logUserIn(new LoginParams(email, password))
                      .enqueue(new Callback<User>() {
                          @Override
                          public void onResponse(Response<User> response,
                                                 Retrofit retrofit) {
                              // Get api token from response
                              mToken = new Token();
                              mToken.setAccess_token(response.headers().get("access-token"));
                              mToken.setClient(response.headers().get("client"));
                              mToken.setExpiry(response.headers().getDate("expiry"));
                              mToken.setToken_type(response.headers().get("token-type"));
                              mToken.setUid(response.headers().get("uid"));

                              currentUser = response.body();
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.e(TAG, "Couldn't create the session when logging in", t);
                              callback.failure(t);
                          }
                      });
    }

    public void getAnnouncements(final HackathonCallback<List<Announcement>> callback) {
        networkService.getAnnouncements()
                      .enqueue(new Callback<AnnouncementList>() {
                          @Override
                          public void onResponse(Response<AnnouncementList> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              List<Announcement> announcements = response.body().getResults();

                              // Sorts reverse chronologically
                              Collections.sort(announcements, new Comparator<Announcement>() {
                                  @Override
                                  public int compare(Announcement lhs,
                                                     Announcement rhs) {
                                      return rhs.getBroadcastTime()
                                                .compareTo(lhs.getBroadcastTime());
                                  }
                              });

                              callback.success(announcements
                                              );
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.e(TAG, "Couldn't get announcements", t);
                              callback.failure(t);
                          }
                      });
    }

    public void getAnnouncement(String announcementId, final HackathonCallback<Announcement> callback) {
        networkService.getAnnouncement(announcementId)
                      .enqueue(new Callback<Announcement>() {
                          @Override
                          public void onResponse(Response<Announcement> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully got the announcement");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the announcement");
                              callback.failure(t);
                          }
                      });
    }

    public void createAnnouncement(Announcement announcement, final HackathonCallback<Announcement> callback) {
        networkService.createAnnouncement(mToken.getAccess_token(),
                                          mToken.getClient(),
                                          mToken.getExpiry(),
                                          mToken.getToken_type(),
                                          mToken.getUid(),
                                          announcement)
                      .enqueue(new Callback<Announcement>() {
                          @Override
                          public void onResponse(Response<Announcement> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully created the announcement");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the announcement");
                              callback.failure(t);
                          }
                      });
    }

    public void updateAnnouncement(Announcement announcement, final HackathonCallback<Announcement> callback) {
        networkService.updateAnnouncement(mToken.getAccess_token(),
                                          mToken.getClient(),
                                          mToken.getExpiry(),
                                          mToken.getToken_type(),
                                          mToken.getUid(),
                                          announcement.getId(),
                                          announcement)
                      .enqueue(new Callback<Announcement>() {
                          @Override
                          public void onResponse(Response<Announcement> response,
                                                 Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully updated the announcement");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the announcement");
                              callback.failure(t);
                          }
                      });
    }

    public void deleteAnnouncement(Announcement announcement, final HackathonCallback<GenericResponse> callback) {
        networkService.deleteAnnouncement(mToken.getAccess_token(),
                                          mToken.getClient(),
                                          mToken.getExpiry(),
                                          mToken.getToken_type(),
                                          mToken.getUid(),
                                          announcement.id)
                      .enqueue(new Callback<GenericResponse>() {
                          @Override
                          public void onResponse(Response<GenericResponse> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully deleted the announcement");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't delete the announcement");
                              callback.failure(t);
                          }
                      });
    }

    public void getEvents(final HackathonCallback<List<Event>> callback) {
        networkService.getEvents()
                      .enqueue(new Callback<EventList>() {
                          @Override
                          public void onResponse(Response<EventList> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully got " + response.body().getResults().size() + " events");

                              List<Event> events = response.body().getResults();

                              // Sorts chronologically
                              Collections.sort(events, new Comparator<Event>() {
                                  @Override
                                  public int compare(Event lhs, Event rhs) {
                                      return lhs.getStartTime().compareTo(rhs.getStartTime());
                                  }
                              });

                              callback.success(events);
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.e(TAG, "Couldn't get the events", t);
                              callback.failure(t);
                          }
                      });
    }

    public void getEvent(String eventId, final HackathonCallback<Event> callback) {
        networkService.getEvent(eventId)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully got the event");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the event");
                              callback.failure(t);
                          }
                      });
    }

    public void createEvent(Event event, final HackathonCallback<Event> callback) {
        networkService.createEvent(mToken.getAccess_token(),
                                   mToken.getClient(),
                                   mToken.getExpiry(),
                                   mToken.getToken_type(),
                                   mToken.getUid(),
                                   event)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully created the event");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the event");
                              callback.failure(t);
                          }
                      });
    }

    public void updateEvent(Event event, final HackathonCallback<Event> callback) {
        networkService.updateEvent(mToken.getAccess_token(),
                                   mToken.getClient(),
                                   mToken.getExpiry(),
                                   mToken.getToken_type(),
                                   mToken.getUid(),
                                   event.getId(),
                                   event)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully updated the event");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't update the event");
                              callback.failure(t);
                          }
                      });
    }

    public void deleteEvent(Event event, final HackathonCallback<GenericResponse> callback) {
        networkService.deleteEvent(mToken.getAccess_token(),
                                   mToken.getClient(),
                                   mToken.getExpiry(),
                                   mToken.getToken_type(),
                                   mToken.getUid(),
                                   event.id)
                      .enqueue(new Callback<GenericResponse>() {
                          @Override
                          public void onResponse(Response<GenericResponse> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully deleted the event");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't delete the event");
                              callback.failure(t);
                          }
                      });
    }

    public void getLocations(final HackathonCallback<List<Location>> callback) {
        networkService.getLocations()
                      .enqueue(new Callback<List<Location>>() {
                          @Override
                          public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully got " + response.body().size() + " locationIds");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the locationIds");
                              callback.failure(t);
                          }
                      });
    }

    public void createLocation(Location location, final HackathonCallback<Location> callback) {
        networkService.createLocation(mToken.getAccess_token(),
                                      mToken.getClient(),
                                      mToken.getExpiry(),
                                      mToken.getToken_type(),
                                      mToken.getUid(),
                                      location)
                      .enqueue(new Callback<Location>() {
                          @Override
                          public void onResponse(Response<Location> response, Retrofit retrofit) {
                              updateToken(response.headers());

                              Log.d(TAG, "Successfully created the location");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the location");
                              callback.failure(t);
                          }
                      });
    }

    public void getMap(final HackathonCallback<Map> callback) {
        networkService.getMap()
                      .enqueue(new Callback<Map>() {
                          @Override
                          public void onResponse(Response<Map> response, Retrofit retrofit) {
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.e(TAG, "Couldn't get the map", t);
                              callback.failure(t);
                          }
                      });
    }

    public void sendToken(com.mhacks.android.data.model.Token token, final HackathonCallback<com.mhacks.android.data.model.Token> callback) {
        networkService.sendToken(token)
                      .enqueue(new Callback<com.mhacks.android.data.model.Token>() {
                          @Override
                          public void onResponse(Response<com.mhacks.android.data.model.Token> response,
                                                 Retrofit retrofit) {
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              callback.failure(t);
                          }
                      });
    }

    public void getCountdown(final HackathonCallback<Countdown> callback) {
        networkService.getCountdown()
                      .enqueue(new Callback<Countdown>() {
                          @Override
                          public void onResponse(Response<Countdown> response, Retrofit retrofit) {
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              callback.failure(t);
                          }
                      });
    }

    /**
     * Returns the current user logged in via the NetworkManager instance.
     * @return current user if successfully logged in, null otherwise
     */
    public User getCurrentUser() {
        if (instance != null && instance.currentUser != null) return instance.currentUser;
        else return null;
    }

    private void updateToken(Headers headers) {
        if (headers.get("access-token") != null) {
            mToken.setAccess_token(headers.get("access-token"));
            mToken.setClient(headers.get("client"));
            mToken.setExpiry(headers.getDate("expiry"));
            mToken.setToken_type(headers.get("token-type"));
            mToken.setUid(headers.get("uid"));
        }
    }

    // TODO all dat GCM stuff
    private String getGcmToken() {
        return "";
    }
}
