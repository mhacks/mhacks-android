//package com.mhacks.android.data.network;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//
//import com.google.gson.FieldNamingPolicy;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.mhacks.android.data.model.Events;
//import com.mhacks.android.data.model.Countdown;
//import com.mhacks.android.data.model.Event;
//import com.mhacks.android.data.model.Floor;
//import com.mhacks.android.data.model.Location;
//import com.mhacks.android.data.model.Login;
//import com.mhacks.android.data.model.ModelList;
//import com.mhacks.android.data.model.Scan;
//import com.mhacks.android.data.model.ScanEvent;
//import com.mhacks.android.data.model.Token;
//import com.mhacks.android.data.model.User;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by boztalay on 6/4/15.
// * Updated by omkarmoghe on 12/25/15 for the MHacksApplication modular backend & Retrofit 2.0.0-beta2.
// *
// */
//public class NetworkManager {
//
//    private static final String TAG      = "NetworkManager";
//    private static final String BASE_URL = "https://staging.mhacks.org/v1/";
//
//    private HackathonNetworkService networkService;
//    private String                  mToken;
//    private User                    currentUser;
//
//    private static NetworkManager instance;
//
//    public static NetworkManager getInstance() {
//        if (instance == null) {
//            instance = new NetworkManager();
//        }
//
//        return instance;
//    }
//
//    public NetworkManager() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .create();
//
//        // Logging
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);
//
//        MHacksService retrofit = new MHacksService.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(httpClient.build()) // only for logging/debugging
//                .build();
//
//        this.networkService = retrofit.create(HackathonNetworkService.class);
//    }
//
//    public void login(String email, String password, final HackathonCallback<User> callback) {
//        networkService.login(email, password)
//                      .enqueue(new Callback<Login>() {
//                          @Override
//                          public void onResponse(Call<Login> call, Response<Login> response) {
//                              Login login = response.body();
//
//                              if (response.code() >= 400 || login == null) {
//                                  callback.failure(new Exception("Unable to log in"));
//                                  return;
//                              }
//
//                              mToken = "Token " + login.getToken();
//                              currentUser = login.getUser();
//
//                              Log.d(TAG, call.request().toString());
//                              Log.d(TAG, currentUser.getName());
//
//                              callback.success(currentUser);
//                          }
//
//                          @Override
//                          public void onFailure(Call<Login> call, Throwable t) {
//                              currentUser = null;
//
//                              Log.e(TAG, "Couldn't create the session when logging in");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getEvents(final HackathonCallback<List<Events>> callback) {
//        networkService.getEvents(mToken)
//                      .enqueue(new Callback<ModelList<Events>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<Events>> call, Response<ModelList<Events>> response) {
//                              List<Events> events = response.body().getResults();
//
//
//                              // Sorts reverse chronologically
//                              Collections.sort(events, new Comparator<Events>() {
//                                  @Override
//                                  public int compare(Events lhs,
//                                                     Events rhs) {
//                                      return (int) (rhs.getBroadcastAt() - lhs.getBroadcastAt());
//                                  }
//                              });
//
//                              // filter out deleted and non approved events
//                              ArrayList<Events> filtered = new ArrayList<Events>();
//                              for (Events a : events) {
//                                  if (!a.isDeleted() && a.isApproved()) {
//                                      a.setBroadcastAt(a.getBroadcastAt() * 1000);
//                                      filtered.add(a);
//                                  }
//                              }
//
//                              callback.success(filtered);
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<Events>> call, Throwable t) {
//                              Log.e(TAG, "Couldn't get events", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getAnnouncement(String announcementId, final HackathonCallback<Events> callback) {
//        networkService.getAnnouncement(mToken, announcementId)
//                      .enqueue(new Callback<Events>() {
//                          @Override
//                          public void onResponse(Call<Events> call,
//                                                 Response<Events> response) {
//                              Log.d(TAG, "Successfully got the announcement");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Events> call, Throwable t) {
//                              Log.d(TAG, "Couldn't get the announcement");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void createAnnouncement(Events announcement, final HackathonCallback<Events> callback) {
//        networkService.createAnnouncement(mToken, announcement)
//                      .enqueue(new Callback<Events>() {
//                          @Override
//                          public void onResponse(Call<Events> call,
//                                                 Response<Events> response) {
//                              Log.d(TAG, "Successfully created the announcement");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Events> call, Throwable t) {
//                              Log.d(TAG, "Couldn't create the announcement");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void updateAnnouncement(Events announcement, final HackathonCallback<Events> callback) {
//        networkService.updateAnnouncement(announcement.getId(), mToken, announcement)
//                      .enqueue(new Callback<Events>() {
//                          @Override
//                          public void onResponse(Call<Events> call,
//                                                 Response<Events> response) {
//                              Log.d(TAG, "Successfully updated the announcement");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Events> call, Throwable t) {
//                              Log.d(TAG, "Couldn't create the announcement");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void deleteAnnouncement(Events announcement, final HackathonCallback<Events> callback) {
//        networkService.deleteAnnouncement(announcement.getId(), mToken, announcement)
//                      .enqueue(new Callback<Events>() {
//                          @Override
//                          public void onResponse(Call<Events> call,
//                                                 Response<Events> response) {
//
//                              Log.d(TAG, "Successfully deleted the announcement");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Events> call, Throwable t) {
//                              Log.d(TAG, "Couldn't delete the announcement");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getEvents(final HackathonCallback<List<Event>> callback) {
//        networkService.getEvents(mToken)
//                      .enqueue(new Callback<ModelList<Event>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<Event>> call, Response<ModelList<Event>> response) {
//                              Log.d(TAG, "Successfully got " + response.body().getResults().size() + " events");
//
//                              List<Event> events = response.body().getResults();
//
//                              // Sorts chronologically
//                              Collections.sort(events, new Comparator<Event>() {
//                                  @Override
//                                  public int compare(Event lhs, Event rhs) {
//                                      return (int) (lhs.getStart() - rhs.getStart());
//                                  }
//                              });
//
//                              callback.success(events);
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<Event>> call, Throwable t) {
//                              Log.e(TAG, "Couldn't get the events", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getEvent(String eventId, final HackathonCallback<Event> callback) {
//        networkService.getEvent(eventId, mToken)
//                      .enqueue(new Callback<Event>() {
//                          @Override
//                          public void onResponse(Call<Event> call, Response<Event> response) {
//                              Log.d(TAG, "Successfully got the event");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Event> call, Throwable t) {
//                              Log.d(TAG, "Couldn't get the event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void createEvent(Event event, final HackathonCallback<Event> callback) {
//        networkService.createEvent(mToken, event)
//                      .enqueue(new Callback<Event>() {
//                          @Override
//                          public void onResponse(Call<Event> call, Response<Event> response) {
//                              Log.d(TAG, "Successfully created the event");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Event> call, Throwable t) {
//                              Log.d(TAG, "Couldn't create the event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void updateEvent(Event event, final HackathonCallback<Event> callback) {
//        networkService.updateEvent(event.getId(), mToken, event)
//                      .enqueue(new Callback<Event>() {
//                          @Override
//                          public void onResponse(Call<Event> call, Response<Event> response) {
//                              Log.d(TAG, "Successfully updated the event");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Event> call, Throwable t) {
//                              Log.d(TAG, "Couldn't update the event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void deleteEvent(Event event, final HackathonCallback<Event> callback) {
//        networkService.deleteEvent(event.getId(), mToken, event)
//                      .enqueue(new Callback<Event>() {
//                          @Override
//                          public void onResponse(Call<Event> call, Response<Event> response) {
//                              Log.d(TAG, "Successfully deleted the event");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Event> call, Throwable t) {
//                              Log.d(TAG, "Couldn't delete the event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getLocations(final HackathonCallback<List<Location>> callback) {
//        networkService.getLocations(mToken)
//                      .enqueue(new Callback<ModelList<Location>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<Location>> call,
//                                                 Response<ModelList<Location>> response) {
//                              Log.d(TAG, "Successfully got " + response.body().getResults().size() + " locationIds");
//                              callback.success(response.body().getResults());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<Location>> call, Throwable t) {
//                              Log.e(TAG, "Couldn't get the locationIds", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void createLocation(Location location, final HackathonCallback<Location> callback) {
//        networkService.createLocation(mToken, location)
//                      .enqueue(new Callback<Location>() {
//                          @Override
//                          public void onResponse(Call<Location> call, Response<Location> response) {
//                              Log.d(TAG, "Successfully created the location");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Location> call, Throwable t) {
//                              Log.d(TAG, "Couldn't create the location");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getLocation(String locationId, final HackathonCallback<Location> callback) {
//        networkService.getLocation(locationId, mToken)
//                      .enqueue(new Callback<Location>() {
//                          @Override
//                          public void onResponse(Call<Location> call, Response<Location> response) {
//                              Location l = response.body();
//                              if (l == null) Log.d(TAG, "fuck, the location is null");
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Location> call, Throwable t) {
//                              Log.e(TAG, "unable to get location");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void updateLocation(final Location location, final HackathonCallback<Location> callback) {
//        networkService.updateLocation(location.getId(), mToken, location)
//                      .enqueue(new Callback<Location>() {
//                          @Override
//                          public void onResponse(Call<Location> call, Response<Location> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Location> call, Throwable t) {
//                              Log.e(TAG, "unable to update location, id: " + location.getId());
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void deleteLocation(final Location location, final HackathonCallback<Location> callback) {
//        networkService.deleteLocation(location.getId(), mToken, location)
//                      .enqueue(new Callback<Location>() {
//                          @Override
//                          public void onResponse(Call<Location> call, Response<Location> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Location> call, Throwable t) {
//                              Log.e(TAG, "unable to get location, id: " + location.getId());
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void sendToken(Token token, final HackathonCallback<Token> callback) {
//        networkService.sendGcmToken(mToken, token.getName(), token.getRegistrationId(), token.isActive())
//                      .enqueue(new Callback<Token>() {
//                          @Override
//                          public void onResponse(Call<Token> call, Response<Token> response) {
//                              if (response.code() >= 400) callback.failure(new Exception("response code: " + response.code()));
//
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Token> call, Throwable t) {
//                              Log.e(TAG, "unable to send GCM token", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getFloors(final HackathonCallback<List<Floor>> callback) {
//        networkService.getFloors(mToken)
//                      .enqueue(new Callback<ModelList<Floor>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<Floor>> call,
//                                                 Response<ModelList<Floor>> response) {
//                              List<Floor> floors = response.body().getResults();
//                              Collections.sort(floors, new Comparator<Floor>() {
//                                  @Override
//                                  public int compare(Floor floor, Floor t1) {
//                                      return floor.getIndex() - t1.getIndex();
//                                  }
//                              });
//
//                              callback.success(floors);
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<Floor>> call, Throwable t) {
//                              Log.e(TAG, "unable to get floors");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void createFloor(Floor floor, final HackathonCallback<List<Floor>> callback) {
//        networkService.createFloor(mToken, floor)
//                      .enqueue(new Callback<ModelList<Floor>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<Floor>> call,
//                                                 Response<ModelList<Floor>> response) {
//                              callback.success(response.body().getResults());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<Floor>> call, Throwable t) {
//                              Log.d(TAG, "unable to create floor");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getFloor(String floorId, final HackathonCallback<Floor> callback) {
//        networkService.getFloor(floorId, mToken)
//                      .enqueue(new Callback<Floor>() {
//                          @Override
//                          public void onResponse(Call<Floor> call, Response<Floor> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Floor> call, Throwable t) {
//                              Log.e(TAG, "unable to get floor");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void updateFloor(Floor floor, final HackathonCallback<Floor> callback) {
//        networkService.updateFloor(floor.getId(), mToken, floor)
//                      .enqueue(new Callback<Floor>() {
//                          @Override
//                          public void onResponse(Call<Floor> call, Response<Floor> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Floor> call, Throwable t) {
//                              Log.e(TAG, "unable to get floor");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void deleteFloor(Floor floor, final HackathonCallback<Floor> callback) {
//        networkService.deleteFloor(floor.getId(), mToken, floor)
//                      .enqueue(new Callback<Floor>() {
//                          @Override
//                          public void onResponse(Call<Floor> call, Response<Floor> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Floor> call, Throwable t) {
//                              Log.e(TAG, "unable to delete floor");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getCountdown(final HackathonCallback<Countdown> callback) {
//        networkService.getCountdown()
//                      .enqueue(new Callback<Countdown>() {
//                          @Override
//                          public void onResponse(Call<Countdown> call,
//                                                 Response<Countdown> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Countdown> call, Throwable t) {
//                              Log.e(TAG, "unable to get countdown");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getScanEvents(final HackathonCallback<List<ScanEvent>> callback) {
//        networkService.getScanEvents(mToken)
//                      .enqueue(new Callback<ModelList<ScanEvent>>() {
//                          @Override
//                          public void onResponse(Call<ModelList<ScanEvent>> call,
//                                                 Response<ModelList<ScanEvent>> response) {
//                              if (response.code() >= 400) {
//                                  callback.failure(new Exception("response code: " + response.code()));
//                                  return;
//                              }
//
//                              List<ScanEvent> temp = response.body().getResults();
//                              ArrayList<ScanEvent> filtered = new ArrayList<>();
//
//                              Date current = new Date();
//                              for (ScanEvent scanEvent : temp) {
//                                  if (scanEvent.getExpiryDate() == 0) filtered.add(scanEvent);
//                                  else {
//                                      Date scanEventExpiry = new Date(scanEvent.getExpiryDate() * 1000);
//
//                                      if (current.before(scanEventExpiry)) filtered.add(scanEvent);
//                                  }
//                              }
//
//                              callback.success(filtered);
//                          }
//
//                          @Override
//                          public void onFailure(Call<ModelList<ScanEvent>> call, Throwable t) {
//                              Log.e(TAG, call.request().toString());
//                              Log.e(TAG, "unable to get scan events");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void createScanEvent(ScanEvent scanEvent, final HackathonCallback<ScanEvent> callback) {
//        networkService.createScanEvent(mToken, scanEvent)
//                      .enqueue(new Callback<ScanEvent>() {
//                          @Override
//                          public void onResponse(Call<ScanEvent> call,
//                                                 Response<ScanEvent> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ScanEvent> call, Throwable t) {
//                              Log.e(TAG, "unable to create scan event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getScanEvent(String scanEventId, final HackathonCallback<ScanEvent> callback) {
//        networkService.getScanEvent(scanEventId, mToken)
//                      .enqueue(new Callback<ScanEvent>() {
//                          @Override
//                          public void onResponse(Call<ScanEvent> call,
//                                                 Response<ScanEvent> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ScanEvent> call, Throwable t) {
//                              Log.e(TAG, "unable to create scan event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void updateScanEvent(ScanEvent scanEvent, final HackathonCallback<ScanEvent> callback) {
//        networkService.updateScanEvent(scanEvent.getId(), mToken, scanEvent)
//                      .enqueue(new Callback<ScanEvent>() {
//                          @Override
//                          public void onResponse(Call<ScanEvent> call,
//                                                 Response<ScanEvent> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ScanEvent> call, Throwable t) {
//                              Log.e(TAG, "unable to update scan event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void deleteScanEvent(ScanEvent scanEvent, final HackathonCallback<ScanEvent> callback) {
//        networkService.deleteScanEvent(scanEvent.getId(), mToken, scanEvent)
//                      .enqueue(new Callback<ScanEvent>() {
//                          @Override
//                          public void onResponse(Call<ScanEvent> call,
//                                                 Response<ScanEvent> response) {
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<ScanEvent> call, Throwable t) {
//                              Log.e(TAG, "unable to delete scan event");
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void performScan(String email, String scanId, final HackathonCallback<Scan> callback) {
//        networkService.performScan(mToken, email, scanId)
//                      .enqueue(new Callback<Scan>() {
//                          @Override
//                          public void onResponse(Call<Scan> call, Response<Scan> response) {
//                              if (response.code() >= 400) {
//                                  callback.failure(new Exception("response code: " + response.code()));
//                                  return;
//                              }
//
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Scan> call, Throwable t) {
//                              Log.e(TAG, "unable to perform the scan", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void confirmScan(String email, String scanId, final HackathonCallback<Scan> callback) {
//        networkService.confirmScan(mToken, email, scanId)
//                      .enqueue(new Callback<Scan>() {
//                          @Override
//                          public void onResponse(Call<Scan> call, Response<Scan> response) {
//                              if (response.code() >= 400) {
//                                  callback.failure(new Exception("response code: " + response.code()));
//                                  return;
//                              }
//
//                              callback.success(response.body());
//                          }
//
//                          @Override
//                          public void onFailure(Call<Scan> call, Throwable t) {
//                              Log.e(TAG, "unable to confirm the scan", t);
//                              callback.failure(t);
//                          }
//                      });
//    }
//
//    public void getImage(String url, final HackathonCallback<Bitmap> callback) {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response)
//                    throws IOException {
//                Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
//
//                callback.success(image);
//            }
//        });
//    }
//
//    public void logout() {
//        currentUser = null;
//        mToken = null;
//    }
//
//    /**
//     * Returns the current user logged in via the NetworkManager instance.
//     * @return current user if successfully logged in, null otherwise
//     */
//    public User getCurrentUser() {
//        if (instance != null && instance.currentUser != null) return instance.currentUser;
//        else return null;
//    }
//}
