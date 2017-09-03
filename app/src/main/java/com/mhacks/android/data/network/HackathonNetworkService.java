package com.mhacks.android.data.network;

import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Countdown;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Floor;
import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.model.Login;
import com.mhacks.android.data.model.ModelList;
import com.mhacks.android.data.model.Scan;
import com.mhacks.android.data.model.ScanEvent;
import com.mhacks.android.data.model.Token;
import com.mhacks.android.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by boztalay on 6/3/15 for the OneHack backend.
 * Updated by omkarmoghe on 12/25/15 for the MHacksApplication modular backend.
 *
 * NOTE: DO NOT begin endpoints with a '/'.
 *       See com.mhacks.android.data.network.NetworkManager.BASE_URL for path.
 */
public interface HackathonNetworkService {

    // USERS
    @FormUrlEncoded
    @POST("login/")
    Call<Login> login(@Field("username") String username,
                      @Field("password") String password);

    @GET("profile")
    Call<User> profile(@Header("Authorization") String authToken);

    // ANNOUNCEMENTS
    @GET("announcements")
    Call<ModelList<Announcement>> getAnnouncements(@Header("Authorization") String authToken);

    @POST("announcements")
    Call<Announcement> createAnnouncement(@Header("Authorization") String authToken,
                                          @Body Announcement announcement);

    @GET("announcements/{id}")
    Call<Announcement> getAnnouncement(@Path("id") String id,
                                       @Header("Authorization") String authToken);


    @PUT("announcements/{id}")
    Call<Announcement> updateAnnouncement(@Path("id") String id,
                                          @Header("Authorization") String authToken,
                                          @Body Announcement announcement);

    @DELETE("announcements/{id}")
    Call<Announcement> deleteAnnouncement(@Path("id") String id,
                                          @Header("Authorization") String authToken,
                                          @Body Announcement announcement);

    // LOCATIONS
    @GET("locations")
    Call<ModelList<Location>> getLocations(@Header("Authorization") String authToken);

    @POST("locations")
    Call<Location> createLocation(@Header("Authorization") String authToken,
                                  @Body Location location);

    @GET("locations/{id}")
    Call<Location> getLocation(@Path("id") String id,
                               @Header("Authorization") String authToken);

    @PUT("locations/{id}")
    Call<Location> updateLocation(@Path("id") String id,
                                  @Header("Authorization") String authToken,
                                  @Body Location location);

    @DELETE("locations/{id}")
    Call<Location> deleteLocation(@Path("id") String id,
                                  @Header("Authorization") String authToken,
                                  @Body Location location);

    // EVENTS
    @GET("events")
    Call<ModelList<Event>> getEvents(@Header("Authorization") String authToken);

    @POST("events")
    Call<Event> createEvent(@Header("Authorization") String authToken,
                            @Body Event event);

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") String id,
                         @Header("Authorization") String authToken);

    @PUT("events/{id}")
    Call<Event> updateEvent(@Path("id") String id,
                            @Header("Authorization") String authToken,
                            @Body Event event);

    @DELETE("events/{id}")
    Call<Event> deleteEvent(@Path("id") String id,
                            @Header("Authorization") String authToken,
                            @Body Event event);

    // FLOORS
    @GET("floors")
    Call<ModelList<Floor>> getFloors(@Header("Authorization") String authToken);

    @POST("floors")
    Call<ModelList<Floor>> createFloor(@Header("Authorization") String authToken,
                                       @Body Floor floor);

    @GET("floors/{id}")
    Call<Floor> getFloor(@Path("id") String id,
                         @Header("Authorization") String authToken);

    @PUT("floors/{id}")
    Call<Floor> updateFloor(@Path("id") String id,
                            @Header("Authorization") String authToken,
                            @Body Floor floor);

    @DELETE("floors/{id}")
    Call<Floor> deleteFloor(@Path("id") String id,
                            @Header("Authorization") String authToken,
                            @Body Floor floor);

    // SCAN EVENTS
    @GET("scan_events")
    Call<ModelList<ScanEvent>> getScanEvents(@Header("Authorization") String authToken);

    @POST("scan_event")
    Call<ScanEvent> createScanEvent(@Header("Authorization") String authToken,
                                    @Body ScanEvent scanEvent);

    @GET("scan_event/{id}")
    Call<ScanEvent> getScanEvent(@Path("id") String id,
                                 @Header("Authorization") String authToken);

    @PUT("scan_event/{id}")
    Call<ScanEvent> updateScanEvent(@Path("id") String id,
                                    @Header("Authorization") String authToken,
                                    @Body ScanEvent scanEvent);

    @DELETE("scan_event/{id}")
    Call<ScanEvent> deleteScanEvent(@Path("id") String id,
                                    @Header("Authorization") String authToken,
                                    @Body ScanEvent scanEvent);

    @GET("perform_scan")
    Call<Scan> performScan(@Header("Authorization") String authToken,
                           @Query("user_id") String email,
                           @Query("scan_event") String scanId);

    @FormUrlEncoded
    @POST("perform_scan/")
    Call<Scan> confirmScan(@Header("Authorization") String authToken,
                           @Field("user_id") String email,
                           @Field("scan_event") String scanId);

    @GET("countdown")
    Call<Countdown> getCountdown();

    // PUSH NOTIFICATIONS
    @FormUrlEncoded
    @POST("push_notifications/gcm/")
    Call<Token> sendGcmToken(@Header("Authorization") String authToken,
                             @Field("name") String channelPrefs,
                             @Field("registration_id") String regId,
                             @Field("active") Boolean active);
}
