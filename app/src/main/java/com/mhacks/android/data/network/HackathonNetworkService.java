package com.mhacks.android.data.network;

import com.mhacks.android.data.model.*;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
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
    // USER
    @GET("users/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @POST("users/")
    Call<ModelObject> signUserUp(@Body User user);

    @POST("sessions")
    Call<ModelObject> logUserIn(@Body LoginParams loginParams);

    @DELETE("sessions")
    Call<GenericResponse> logUserOut();

    // HACKATHONS
    // deprecated as of MHacks Refactor W16
    @GET("hackathons")
    Call<List<Hackathon>> getHackathons(@Header("Authorization") String token);

    @GET("hackathons/attending")
    Call<List<Hackathon>> getAttendingHackathons(@Header("Authorization") String token);

    @GET("hackathons/{hackathon_id}")
    Call<Hackathon> getHackathon(@Header("Authorization") String token,
                                 @Path("hackathon_id") int hackathon_id);

    @POST("hackathons")
    Call<Hackathon> createHackathon(@Header("Authorization") String token,
                                    @Body Hackathon hackathon);

    // ANNOUNCEMENTS
    @GET("announcements")
    Call<List<Announcement>> getAnnouncements();

    @GET("announcements/{announcement_id}")
    Call<Announcement> getAnnouncement(@Path("announcement_id") int announcement_id);

    @POST("announcements")
    Call<Announcement> createAnnouncement(@Header("Authorization") String token,
                                          @Body Announcement announcement);

    @DELETE("announcements/{announcement_id}")
    Call<GenericResponse> deleteAnnouncement(@Header("Authorization") String token,
                                             @Path("announcement_id") int announcement_id);

    // EVENTS
    @GET("events")
    Call<List<Event>> getEvents();

    @GET("events/{event_id}")
    Call<Event> getEvent(@Path("event_id") int event_id);

    @POST("events")
    Call<Event> createEvent(@Header("Authorization") String token,
                            @Body Event event);

    @PUT("events/{event_id}")
    Call<Event> updateEvent(@Header("Authorization") String token,
                            @Path("event_id") int event_id,
                            @Body Event event);

    @DELETE("events/{event_id}")
    Call<GenericResponse> deleteEvent(@Header("Authorization") String token,
                                      @Path("event_id") int event_id);

    // LOCATIONS
    @GET("locations")
    Call<List<Location>> getLocations();

    @POST("locations")
    Call<Location> createLocation(@Header("Authorization") String token,
                                  @Body Location location);

    // CONTACTS
    @GET("contacts")
    Call<List<User>> getContacts(@Header("Authorization") String token);

    // ROLES
    @POST("hacker_roles")
    Call<HackerRole> createHackerRole(@Header("Authorization") String token,
                                      @Body HackerRole hackerRole);

    @PUT("hacker_roles/{hacker_role_id}")
    Call<HackerRole> updateHackerRole(@Header("Authorization") String token,
                                      @Path("hacker_role_id") int hacker_role_id,
                                      @Body HackerRole hackerRole);

    // AWARDS
    @GET("awards")
    Call<List<Award>> getAwards();

    @POST("awards")
    Call<Award> createAward(@Header("Authorization") String token,
                            @Body Award award);
}
