package com.mhacks.android.data.network;

import android.util.Log;

import com.mhacks.android.data.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Response;

/**
 * Created by boztalay on 6/4/15.
 */
public class NetworkManager {

    private static final String tag     = "ONEHACK-NM";
    private static final String baseUrl = "http://onehack-mhacks.herokuapp.com/v1";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private OneHackNetworkService networkService;
    private String                apiToken;
    private ArrayList<Hackathon>  hackathonsAttending;
    private Hackathon             currentHackathon;
    private User                  currentUser;

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
                //.setLogLevel(Retrofit.LogLevel.FULL)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.networkService = retrofit.create(OneHackNetworkService.class);

        hackathonsAttending = new ArrayList<Hackathon>();
    }

    public void logUserIn(String email, String password, final OneHackCallback<User> callback) {
        networkService.logUserIn(new LoginParams(email, password, getGcmToken()),
                                 new Callback<ModelObject>() {
                                     @Override
                                     public void success(ModelObject token, Response response) {
                                         Log.d(tag, "Successfully logged in");
                                         apiToken = token.token;

                                         // Now that we have the token, go get the user
                                         networkService.getCurrentUser(apiToken,
                                                                       new Callback<User>() {
                                                                           @Override
                                                                           public void success(final User user,
                                                                                               Response response) {
                                                                               Log.d(tag,
                                                                                     "Successfully got the current user");

                                                                               // Finally, get all of the hackathons that the user is attending
                                                                               getAttendingHackathons(
                                                                                       new OneHackCallback<List<Hackathon>>() {
                                                                                           @Override
                                                                                           public void success(
                                                                                                   List<Hackathon> hackathons) {
                                                                                               Log.d(tag,
                                                                                                     "Successfully got the attending hackathons");

                                                                                               currentUser =
                                                                                                       user;
                                                                                               hackathonsAttending
                                                                                                       .clear();
                                                                                               hackathonsAttending
                                                                                                       .addAll(hackathons);
                                                                                               setCurrentHackathon();

                                                                                               callback.success(
                                                                                                       user);
                                                                                           }

                                                                                           @Override
                                                                                           public void failure(
                                                                                                   Throwable error) {
                                                                                               Log.d(tag,
                                                                                                     "Failed to get the attending hackathons while logging in");
                                                                                               callback.failure(
                                                                                                       error);
                                                                                           }
                                                                                       });
                                                                           }

                                                                           @Override
                                                                           public void failure(
                                                                                   RetrofitError retrofitError) {
                                                                               Log.d(tag,
                                                                                     "Couldn't get the current user when logging in");
                                                                               callback.failure(
                                                                                       retrofitError);
                                                                           }
                                                                       });
                                     }

                                     @Override
                                     public void failure(RetrofitError retrofitError) {
                                         Log.d(tag, "Couldn't create the session when logging in");
                                         callback.failure(retrofitError);
                                     }
                                 });
    }

    public void signUserUp(String email, String password, String firstName, String lastName, String company, final OneHackCallback<User> callback) {
        User user = new User();
        user.gcm_token = getGcmToken();
        user.email = email;
        user.password = password;
        user.firstName = firstName;
        user.lastName = lastName;
        user.company = company;

        networkService.signUserUp(user, new Callback<ModelObject>() {
            @Override
            public void success(ModelObject token, Response response) {
                apiToken = token.token;

                // Now that we have the token, go get the user
                networkService.getCurrentUser(apiToken, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        Log.d(tag, "Successfully signed the user up");
                        callback.success(user);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d(tag, "Couldn't get the current user after signing up");
                        callback.failure(retrofitError);
                    }
                });
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't create the new user");
                callback.failure(retrofitError);
            }
        });
    }

    public void logUserOut(final OneHackCallback<Void> callback) {
        networkService.logUserOut(new Callback<GenericResponse>() {
            @Override
            public void success(GenericResponse genericResponse, Response response) {
                Log.d(tag, "Successfully logged the user out");
                callback.success(null);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't log the user out");
                callback.failure(retrofitError);
            }
        });
    }

    public void getHackathons(final OneHackCallback<List<Hackathon>> callback) {
        networkService.getHackathons(apiToken, new Callback<List<Hackathon>>() {
            @Override
            public void success(List<Hackathon> hackathons, Response response) {
                Log.d(tag, "Successfully got " + hackathons.size() + " hackathons");

                // Sorts alphabetically
                Collections.sort(hackathons, new Comparator<Hackathon>() {
                    @Override
                    public int compare(Hackathon lhs, Hackathon rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                callback.success(hackathons);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get hackathons");
                callback.failure(retrofitError);
            }
        });
    }

    public void getAttendingHackathons(final OneHackCallback<List<Hackathon>> callback) {
        networkService.getAttendingHackathons(apiToken, new Callback<List<Hackathon>>() {
            @Override
            public void success(List<Hackathon> hackathons, Response response) {
                Log.d(tag, "Successfully got " + hackathons.size() + " attending hackathons");

                // Sorts alphabetically
                Collections.sort(hackathons, new Comparator<Hackathon>() {
                    @Override
                    public int compare(Hackathon lhs, Hackathon rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                callback.success(hackathons);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get attending hackathons");
                callback.failure(retrofitError);
            }
        });
    }

    public void getHackathon(int hackathonId, final OneHackCallback<Hackathon> callback) {
        networkService.getHackathon(apiToken, hackathonId, new Callback<Hackathon>() {
            @Override
            public void success(Hackathon hackathon, Response response) {
                Log.d(tag, "Successfully got the hackathon " + hackathon.name);
                callback.success(hackathon);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get a hackathon");
                callback.failure(retrofitError);
            }
        });
    }

    public void createHackathon(Hackathon hackathon, final OneHackCallback<Hackathon> callback) {
        networkService.createHackathon(apiToken, hackathon, new Callback<Hackathon>() {
            @Override
            public void success(Hackathon hackathon, Response response) {
                Log.d(tag, "Successfully created the hackathon " + hackathon.name);
                callback.success(hackathon);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't create the hackathon");
                callback.failure(retrofitError);
            }
        });
    }

    public void getAnnouncements(final OneHackCallback<List<Announcement>> callback) {
        networkService.getAnnouncements(currentHackathon.getId(),
                                        new Callback<List<Announcement>>() {
                                            @Override
                                            public void success(List<Announcement> announcements,
                                                                Response response) {
                                                Log.d(tag,
                                                      "Successfully got " + announcements.size() +
                                                      " announcements");

                                                // Sorts reverse chronologically
                                                Collections.sort(announcements,
                                                                 new Comparator<Announcement>() {
                                                                     @Override
                                                                     public int compare(Announcement lhs,
                                                                                        Announcement rhs) {
                                                                         return rhs.getBroadcastTime()
                                                                                   .compareTo(lhs.getBroadcastTime());
                                                                     }
                                                                 });

                                                callback.success(announcements);
                                            }

                                            @Override
                                            public void failure(RetrofitError retrofitError) {
                                                Log.d(tag, "Couldn't get announcements");
                                                callback.failure(retrofitError);
                                            }
                                        });
    }

    public void getAnnouncement(int announcementId, final OneHackCallback<Announcement> callback) {
        networkService.getAnnouncement(currentHackathon.getId(),
                                       announcementId,
                                       new Callback<Announcement>() {
                                           @Override
                                           public void success(Announcement announcement,
                                                               Response response) {
                                               Log.d(tag, "Successfully got the announcement");
                                               callback.success(announcement);
                                           }

                                           @Override
                                           public void failure(RetrofitError retrofitError) {
                                               Log.d(tag, "Couldn't get the announcement");
                                               callback.failure(retrofitError);
                                           }
                                       });
    }

    public void createAnnouncement(Announcement announcement, final OneHackCallback<Announcement> callback) {
        networkService.createAnnouncement(apiToken,
                                          currentHackathon.getId(),
                                          announcement,
                                          new Callback<Announcement>() {
                                              @Override
                                              public void success(Announcement announcement,
                                                                  Response response) {
                                                  Log.d(tag,
                                                        "Succcessfully created the announcement");
                                                  callback.success(announcement);
                                              }

                                              @Override
                                              public void failure(RetrofitError retrofitError) {
                                                  Log.d(tag, "Couldn't create the announcement");
                                                  callback.failure(retrofitError);
                                              }
                                          });
    }

    public void deleteAnnouncement(Announcement announcement, final OneHackCallback<GenericResponse> callback) {
        networkService.deleteAnnouncement(apiToken,
                                          currentHackathon.getId(),
                                          announcement.id,
                                          new Callback<GenericResponse>() {
                                              @Override
                                              public void success(GenericResponse genericResponse,
                                                                  Response response) {
                                                  Log.d(tag,
                                                        "Successfully deleted the announcement");
                                                  callback.success(genericResponse);
                                              }

                                              @Override
                                              public void failure(RetrofitError retrofitError) {
                                                  Log.d(tag, "Couldn't delete the announcement");
                                                  callback.failure(retrofitError);
                                              }
                                          });
    }

    public void getEvents(final OneHackCallback<List<Event>> callback) {
        networkService.getEvents(currentHackathon.getId(), new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                Log.d(tag, "Successfully got " + events.size() + " events");

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
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get the events");
                callback.failure(retrofitError);
            }
        });
    }

    public void getEvent(int eventId, final OneHackCallback<Event> callback) {
        networkService.getEvent(currentHackathon.getId(), eventId, new Callback<Event>() {
            @Override
            public void success(Event event, Response response) {
                Log.d(tag, "Successfully got the event");
                callback.success(event);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get the event");
                callback.failure(retrofitError);
            }
        });
    }

    public void createEvent(Event event, final OneHackCallback<Event> callback) {
        networkService.createEvent(apiToken,
                                   currentHackathon.getId(),
                                   event,
                                   new Callback<Event>() {
                                       @Override
                                       public void success(Event event, Response response) {
                                           Log.d(tag, "Successfully created the event");
                                           callback.success(event);
                                       }

                                       @Override
                                       public void failure(RetrofitError retrofitError) {
                                           Log.d(tag, "Couldn't create the event");
                                           callback.failure(retrofitError);
                                       }
                                   });
    }

    public void updateEvent(Event event, final OneHackCallback<Event> callback) {
        networkService.updateEvent(apiToken,
                                   currentHackathon.getId(),
                                   event.id,
                                   event,
                                   new Callback<Event>() {
                                       @Override
                                       public void success(Event event, Response response) {
                                           Log.d(tag, "Successfully updated the event");
                                           callback.success(event);
                                       }

                                       @Override
                                       public void failure(RetrofitError retrofitError) {
                                           Log.d(tag, "Couldn't update the event");
                                           callback.failure(retrofitError);
                                       }
                                   });
    }

    public void deleteEvent(Event event, final OneHackCallback<GenericResponse> callback) {
        networkService.deleteEvent(apiToken,
                                   currentHackathon.getId(),
                                   event.id,
                                   new Callback<GenericResponse>() {
                                       @Override
                                       public void success(GenericResponse genericResponse,
                                                           Response response) {
                                           Log.d(tag, "Successfully deleted the event");
                                           callback.success(genericResponse);
                                       }

                                       @Override
                                       public void failure(RetrofitError retrofitError) {
                                           Log.d(tag, "Couldn't delete the event");
                                           callback.failure(retrofitError);
                                       }
                                   });
    }

    public void getLocations(final OneHackCallback<List<Location>> callback) {
        networkService.getLocations(currentHackathon.getId(), new Callback<List<Location>>() {
            @Override
            public void success(List<Location> locations, Response response) {
                Log.d(tag, "Successfully got " + locations.size() + " locations");
                callback.success(locations);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get the locations");
                callback.failure(retrofitError);
            }
        });
    }

    public void createLocation(Location location, final OneHackCallback<Location> callback) {
        networkService.createLocation(apiToken,
                                      currentHackathon.getId(),
                                      location,
                                      new Callback<Location>() {
                                          @Override
                                          public void success(Location location,
                                                              Response response) {
                                              Log.d(tag, "Successfully created the location");
                                              callback.success(location);
                                          }

                                          @Override
                                          public void failure(RetrofitError retrofitError) {
                                              Log.d(tag, "Couldn't create the location");
                                              callback.failure(retrofitError);
                                          }
                                      });
    }

    public void getContacts(final OneHackCallback<List<User>> callback) {
        networkService.getContacts(apiToken, currentHackathon.getId(), new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                Log.d(tag, "Successfully got " + users.size() + " contacts");
                callback.success(users);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get the contacts");
                callback.failure(retrofitError);
            }
        });
    }

    public void createHackerRole(HackerRole hackerRole, final OneHackCallback<HackerRole> callback) {
        networkService.createHackerRole(apiToken, hackerRole, new Callback<HackerRole>() {
            @Override
            public void success(HackerRole hackerRole, Response response) {
                Log.d(tag, "Successfully created the hacker role");
                callback.success(hackerRole);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't create the hacker role");
                callback.failure(retrofitError);
            }
        });
    }

    public void updateHackerRole(HackerRole hackerRole, final OneHackCallback<HackerRole> callback) {
        networkService.updateHackerRole(apiToken,
                                        hackerRole.id,
                                        hackerRole,
                                        new Callback<HackerRole>() {
                                            @Override
                                            public void success(HackerRole hackerRole,
                                                                Response response) {
                                                Log.d(tag, "Successfully updated the hacker role");
                                                callback.success(hackerRole);
                                            }

                                            @Override
                                            public void failure(RetrofitError retrofitError) {
                                                Log.d(tag, "Couldn't update the hacker role");
                                                callback.failure(retrofitError);
                                            }
                                        });
    }

    public void getAwards(final OneHackCallback<List<Award>> callback) {
        networkService.getAwards(currentHackathon.getId(), new Callback<List<Award>>() {
            @Override
            public void success(List<Award> awards, Response response) {
                Log.d(tag, "Successfully got " + awards.size() + " awards");
                callback.success(awards);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(tag, "Couldn't get the awards");
                callback.failure(retrofitError);
            }
        });
    }

    public void createAward(Award award, final OneHackCallback<Award> callback) {
        networkService.createAward(apiToken,
                                   currentHackathon.getId(),
                                   award,
                                   new Callback<Award>() {
                                       @Override
                                       public void success(Award award, Response response) {
                                           Log.d(tag, "Successfully created the award");
                                           callback.success(award);
                                       }

                                       @Override
                                       public void failure(RetrofitError retrofitError) {
                                           Log.d(tag, "Couldn't create the award");
                                           callback.failure(retrofitError);
                                       }
                                   });
    }

    //----- Helpers

    public void setCurrentHackathon() {
        if(hackathonsAttending.size() > 0) {
            currentHackathon = hackathonsAttending.get(0);
        } else {
            // TODO make this user-friendlier
            throw new RuntimeException("This user isn't attending any hackathons!");
        }
    }

    public Hackathon getCurrentHackathon() {
        return currentHackathon;
    }

    public boolean isUserHacker() {
        return currentHackathon.isUserHacker(currentUser);
    }

    public boolean isUserOrganizer() {
        return currentHackathon.isUserOrganizer(currentUser);
    }

    public boolean isUserSponsor() {
        return currentHackathon.isUserSponsor(currentUser);
    }

    public boolean isUserVolunteer() {
        return currentHackathon.isUserVolunteer(currentUser);
    }

    // TODO all dat GCM stuff
    private String getGcmToken() {
        return "";
    }
}
