package com.mhacks.android.data.network;

import com.mhacks.android.data.model.*;

import java.util.List;

import retrofit.Callback;
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
 * NOTE: All endpoints must begin with a '/'.
 */
public interface HackathonNetworkService {
    // USER
    @GET("/users/me")
    void getCurrentUser(@Header("Authorization") String token, Callback<User> callback);

    @POST("/users/")
    void signUserUp(@Body User user, Callback<ModelObject> callback);

    @POST("/sessions")
    void logUserIn(@Body LoginParams loginParams, Callback<ModelObject> callback);

    @DELETE("/sessions")
    void logUserOut(Callback<GenericResponse> callback);

    // HACKATHONS
    // deprecated as of MHacks Refactor W16
    @GET("/hackathons")
    void getHackathons(@Header("Authorization") String token, Callback<List<Hackathon>> callback);

    @GET("/hackathons/attending")
    void getAttendingHackathons(@Header("Authorization") String token,
                                Callback<List<Hackathon>> callback);

    @GET("/hackathons/{hackathon_id}")
    void getHackathon(@Header("Authorization") String token,
                      @Path("hackathon_id") int hackathon_id,
                      Callback<Hackathon> callback);

    @POST("/hackathons")
    void createHackathon(@Header("Authorization") String token,
                         @Body Hackathon hackathon,
                         Callback<Hackathon> callback);

    // ANNOUNCEMENTS
    @GET("/announcements")
    void getAnnouncements(Callback<List<Announcement>> callback);

    @GET("/announcements/{announcement_id}")
    void getAnnouncement(@Path("announcement_id") int announcement_id,
                         Callback<Announcement> callback);

    @POST("/announcements")
    void createAnnouncement(@Header("Authorization") String token,
                            @Body Announcement announcement,
                            Callback<Announcement> callback);

    @DELETE("/announcements/{announcement_id}")
    void deleteAnnouncement(@Header("Authorization") String token,
                            @Path("announcement_id") int announcement_id,
                            Callback<GenericResponse> callback);

    // EVENTS
    @GET("/events")
    void getEvents(Callback<List<Event>> callback);

    @GET("/events/{event_id}")
    void getEvent(@Path("event_id") int event_id,
                  Callback<Event> callback);

    @POST("/events")
    void createEvent(@Header("Authorization") String token,
                     @Body Event event,
                     Callback<Event> callback);

    @PUT("/events/{event_id}")
    void updateEvent(@Header("Authorization") String token,
                     @Path("event_id") int event_id,
                     @Body Event event,
                     Callback<Event> callback);

    @DELETE("/events/{event_id}")
    void deleteEvent(@Header("Authorization") String token,
                     @Path("event_id") int event_id,
                     Callback<GenericResponse> callback);

    // LOCATIONS
    @GET("/locations")
    void getLocations(Callback<List<Location>> callback);

    @POST("/locations")
    void createLocation(@Header("Authorization") String token,
                        @Body Location location,
                        Callback<Location> callback);

    // CONTACTS
    @GET("/contacts")
    void getContacts(@Header("Authorization") String token,
                     Callback<List<User>> callback);

    // ROLES
    @POST("/hacker_roles")
    void createHackerRole(@Header("Authorization") String token,
                          @Body HackerRole hackerRole,
                          Callback<HackerRole> callback);

    @PUT("/hacker_roles/{hacker_role_id}")
    void updateHackerRole(@Header("Authorization") String token,
                          @Path("hacker_role_id") int hacker_role_id,
                          @Body HackerRole hackerRole,
                          Callback<HackerRole> callback);

    // AWARDS
    @GET("/awards")
    void getAwards(Callback<List<Award>> callback);

    @POST("/awards")
    void createAward(@Header("Authorization") String token,
                     @Body Award award,
                     Callback<Award> callback);
}
