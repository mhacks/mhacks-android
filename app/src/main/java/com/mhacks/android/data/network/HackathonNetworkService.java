package com.mhacks.android.data.network;

import com.mhacks.android.data.model.*;
import com.squareup.okhttp.Response;

import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by boztalay on 6/3/15 for the OneHack backend.
 * Updated by omkarmoghe on 12/25/15 for the MHacks modular backend.
 *
 * NOTE: DO NOT begin endpoints with a '/'.
 *       See com.mhacks.android.data.network.NetworkManager.BASE_URL for path.
 */
public interface HackathonNetworkService {
    @GET("users/me")
    Call<User> getCurrentUser();

    @POST("users")
    Call<ModelObject> signUserUp(@Body User user);

    @POST("auth/sign_in")
    Call<User> logUserIn(@Body LoginParams loginParams);

    @DELETE("sessions")
    Call<GenericResponse> logUserOut();

    // ANNOUNCEMENTS
    @GET("announcements")
    Call<AnnouncementList> getAnnouncements();

    @GET("announcements/{announcement_id}")
    Call<Announcement> getAnnouncement(@Path("announcement_id") String announcement_id);

    @POST("announcements")
    Call<Announcement> createAnnouncement(@Header("access-token") String accessToken,
                                          @Header("client") String client,
                                          @Header("expiry") Date expiry,
                                          @Header("token-type") String tokenType,
                                          @Header("uid") String uid,
                                          @Body Announcement announcement);

    @PUT("announcements/{announcement_id}")
    Call<Announcement> updateAnnouncement(@Header("access-token") String accessToken,
                                          @Header("client") String client,
                                          @Header("expiry") Date expiry,
                                          @Header("token-type") String tokenType,
                                          @Header("uid") String uid,
                                          @Path("announcement_id") String announcement_id,
                                          @Body Announcement announcement);

    @DELETE("announcements/{announcement_id}")
    Call<GenericResponse> deleteAnnouncement(@Header("access-token") String accessToken,
                                             @Header("client") String client,
                                             @Header("expiry") Date expiry,
                                             @Header("token-type") String tokenType,
                                             @Header("uid") String uid,
                                             @Path("announcement_id") String announcement_id);

    // EVENTS
    @GET("events")
    Call<EventList> getEvents();

    @GET("events/{event_id}")
    Call<Event> getEvent(@Path("event_id") String event_id);

    @POST("events")
    Call<Event> createEvent(@Header("access-token") String accessToken,
                            @Header("client") String client,
                            @Header("expiry") Date expiry,
                            @Header("token-type") String tokenType,
                            @Header("uid") String uid,
                            @Body Event event);

    @PUT("events/{event_id}")
    Call<Event> updateEvent(@Header("access-token") String accessToken,
                            @Header("client") String client,
                            @Header("expiry") Date expiry,
                            @Header("token-type") String tokenType,
                            @Header("uid") String uid,
                            @Path("event_id") String event_id,
                            @Body Event event);

    @DELETE("events/{event_id}")
    Call<GenericResponse> deleteEvent(@Header("access-token") String accessToken,
                                      @Header("client") String client,
                                      @Header("expiry") Date expiry,
                                      @Header("token-type") String tokenType,
                                      @Header("uid") String uid,
                                      @Path("event_id") String event_id);

    // LOCATIONS
    @GET("locations")
    Call<List<Location>> getLocations();

    @POST("locations")
    Call<Location> createLocation(@Header("access-token") String accessToken,
                                  @Header("client") String client,
                                  @Header("expiry") Date expiry,
                                  @Header("token-type") String tokenType,
                                  @Header("uid") String uid,
                                  @Body Location location);

    // MAPS
    @GET("map")
    Call<Map> getMap();
}
