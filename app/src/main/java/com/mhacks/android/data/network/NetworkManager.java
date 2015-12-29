package com.mhacks.android.data.network;

import android.util.Log;

import com.mhacks.android.data.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Response;

/**
 * Created by boztalay on 6/4/15.
 * Updated by omkarmoghe on 12/25/15 for the MHacks modular backend & Retrofit 2.0.0-beta2.
 *
 */
public class NetworkManager {

    private static final String TAG      = "NetworkManager";
    private static final String BASE_URL = "http://testonehack.herokuapp.com/v1/"; // TODO: figure out a better way to build this string.

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private HackathonNetworkService networkService;
    private String                  apiToken;
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
                .setDateFormat(DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.networkService = retrofit.create(HackathonNetworkService.class);
    }

    public void logUserIn(String email, String password, final HackathonCallback<User> callback) {
        networkService.logUserIn(new LoginParams(email, password, getGcmToken()))
                      .enqueue(new Callback<ModelObject>() {
                          @Override
                          public void onResponse(Response<ModelObject> response,
                                                 Retrofit retrofit) {
                              // Get api token from response
                              apiToken = response.body().getToken();

                              // Now that we have the token, go get the user
                              networkService.getCurrentUser(apiToken)
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Response<User> response, Retrofit retrofit) {
                                                    currentUser = response.body();
                                                    callback.success(response.body());
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Log.d(TAG, "Couldn't get the current user when logging in");
                                                    callback.failure(t);
                                                }
                                            });
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the session when logging in");
                              callback.failure(t);
                          }
                      });
    }

    public void signUserUp(String email, String password, String firstName, String lastName, String company, final HackathonCallback<User> callback) {
        User user = new User();
        user.gcm_token = getGcmToken();
        user.email = email;
        user.password = password;
        user.firstName = firstName;
        user.lastName = lastName;
        user.company = company;

        networkService.signUserUp(user)
                      .enqueue(new Callback<ModelObject>() {
                          @Override
                          public void onResponse(Response<ModelObject> response, Retrofit retrofit) {
                              apiToken = response.body().token;

                              // Now that we have the token, go get the user
                              networkService.getCurrentUser(apiToken)
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Response<User> response, Retrofit retrofit) {
                                                    Log.d(TAG, "Successfully signed the user up");
                                                    callback.success(response.body());
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Log.d(TAG, "Couldn't get the current user after signing up");
                                                    callback.failure(t);
                                                }
                                            });
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the new user");
                              callback.failure(t);
                          }
                      });
    }

    public void logUserOut(final HackathonCallback<Void> callback) {
        networkService.logUserOut()
                      .enqueue(new Callback<GenericResponse>() {
                          @Override
                          public void onResponse(Response<GenericResponse> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully logged the user out");
                              callback.success(null);
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't log the user out");
                              callback.failure(t);
                          }
                      });
    }

    public void getAnnouncements(final HackathonCallback<List<Announcement>> callback) {
        networkService.getAnnouncements()
                      .enqueue(new Callback<List<Announcement>>() {
                          @Override
                          public void onResponse(Response<List<Announcement>> response, Retrofit retrofit) {

                              Log.d(TAG,
                                    "Successfully got " + response.body().size() +
                                    " announcements");

                              // Sorts reverse chronologically
                              Collections.sort(response.body(), new Comparator<Announcement>() {
                                  @Override
                                  public int compare(Announcement lhs,
                                                     Announcement rhs) {
                                      return rhs.getBroadcastTime()
                                                .compareTo(lhs.getBroadcastTime());
                                  }
                              });

                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get announcements");
                              callback.failure(t);
                          }
                      });
    }

    public void getAnnouncement(int announcementId, final HackathonCallback<Announcement> callback) {
        networkService.getAnnouncement(announcementId)
                      .enqueue(new Callback<Announcement>() {
                          @Override
                          public void onResponse(Response<Announcement> response, Retrofit retrofit) {
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
        networkService.createAnnouncement(apiToken, announcement)
                      .enqueue(new Callback<Announcement>() {
                          @Override
                          public void onResponse(Response<Announcement> response, Retrofit retrofit) {
                              Log.d(TAG, "Succcessfully created the announcement");
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
        networkService.deleteAnnouncement(apiToken, announcement.id)
                      .enqueue(new Callback<GenericResponse>() {
                          @Override
                          public void onResponse(Response<GenericResponse> response, Retrofit retrofit) {
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
                      .enqueue(new Callback<List<Event>>() {
                          @Override
                          public void onResponse(Response<List<Event>> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully got " + response.body().size() + " events");

                              // Sorts chronologically
                              Collections.sort(response.body(), new Comparator<Event>() {
                                  @Override
                                  public int compare(Event lhs, Event rhs) {
                                      return lhs.getStartTime().compareTo(rhs.getStartTime());
                                  }
                              });

                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the events");
                              callback.failure(t);
                          }
                      });
    }

    public void getEvent(int eventId, final HackathonCallback<Event> callback) {
        networkService.getEvent(eventId)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
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
        networkService.createEvent(apiToken, event)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
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
        networkService.updateEvent(apiToken, event.id, event)
                      .enqueue(new Callback<Event>() {
                          @Override
                          public void onResponse(Response<Event> response, Retrofit retrofit) {
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
        networkService.deleteEvent(apiToken, event.id)
                      .enqueue(new Callback<GenericResponse>() {
                          @Override
                          public void onResponse(Response<GenericResponse> response, Retrofit retrofit) {
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
                              Log.d(TAG, "Successfully got " + response.body().size() + " locations");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the locations");
                              callback.failure(t);
                          }
                      });
    }

    public void createLocation(Location location, final HackathonCallback<Location> callback) {
        networkService.createLocation(apiToken, location)
                      .enqueue(new Callback<Location>() {
                          @Override
                          public void onResponse(Response<Location> response, Retrofit retrofit) {
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

    public void getContacts(final HackathonCallback<List<User>> callback) {
        networkService.getContacts(apiToken)
                      .enqueue(new Callback<List<User>>() {
                          @Override
                          public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully got " + response.body().size() + " contacts");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the contacts");
                              callback.failure(t);
                          }
                      });
    }

    public void createHackerRole(HackerRole hackerRole, final HackathonCallback<HackerRole> callback) {
        networkService.createHackerRole(apiToken, hackerRole)
                      .enqueue(new Callback<HackerRole>() {
                          @Override
                          public void onResponse(Response<HackerRole> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully created the hacker role");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the hacker role");
                              callback.failure(t);
                          }
                      });
    }

    public void updateHackerRole(HackerRole hackerRole, final HackathonCallback<HackerRole> callback) {
        networkService.updateHackerRole(apiToken, hackerRole.id, hackerRole)
                      .enqueue(new Callback<HackerRole>() {
                          @Override
                          public void onResponse(Response<HackerRole> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully updated the hacker role");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't update the hacker role");
                              callback.failure(t);
                          }
                      });
    }

    public void getAwards(final HackathonCallback<List<Award>> callback) {
        networkService.getAwards()
                      .enqueue(new Callback<List<Award>>() {
                          @Override
                          public void onResponse(Response<List<Award>> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully got " + response.body().size() + " awards");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't get the awards");
                              callback.failure(t);
                          }
                      });
    }

    public void createAward(Award award, final HackathonCallback<Award> callback) {
        networkService.createAward(apiToken, award)
                      .enqueue(new Callback<Award>() {
                          @Override
                          public void onResponse(Response<Award> response, Retrofit retrofit) {
                              Log.d(TAG, "Successfully created the award");
                              callback.success(response.body());
                          }

                          @Override
                          public void onFailure(Throwable t) {
                              Log.d(TAG, "Couldn't create the award");
                              callback.failure(t);
                          }
                      });
    }

    // TODO all dat GCM stuff
    private String getGcmToken() {
        return "";
    }
}
