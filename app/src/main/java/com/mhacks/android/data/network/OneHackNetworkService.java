package com.mhacks.android.data.network;

import com.arbrr.onehack.data.model.*;

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
 * Created by boztalay on 6/3/15.
 */
public interface OneHackNetworkService {
    @GET("/users/me")
    void getCurrentUser(@Header("Authorization") String token, Callback<User> callback);

    @POST("/users/")
    void signUserUp(@Body User user, Callback<ModelObject> callback);

    @POST("/sessions")
    void logUserIn(@Body LoginParams loginParams, Callback<ModelObject> callback);

    @DELETE("/sessions")
    void logUserOut(Callback<GenericResponse> callback);

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

    @GET("/hackathons/{hackathon_id}/announcements")
    void getAnnouncements(@Path("hackathon_id") int hackathon_id,
                          Callback<List<Announcement>> callback);

    @GET("/hackathons/{hackathon_id}/announcements/{announcement_id}")
    void getAnnouncement(@Path("hackathon_id") int hackathon_id,
                         @Path("announcement_id") int announcement_id,
                         Callback<Announcement> callback);

    @POST("/hackathons/{hackathon_id}/announcements")
    void createAnnouncement(@Header("Authorization") String token,
                            @Path("hackathon_id") int hackathon_id,
                            @Body Announcement announcement,
                            Callback<Announcement> callback);

    @DELETE("/hackathons/{hackathon_id}/announcements/{announcement_id}")
    void deleteAnnouncement(@Header("Authorization") String token,
                            @Path("hackathon_id") int hackathon_id,
                            @Path("announcement_id") int announcement_id,
                            Callback<GenericResponse> callback);

    @GET("/hackathons/{hackathon_id}/events")
    void getEvents(@Path("hackathon_id") int hackathon_id, Callback<List<Event>> callback);

    @GET("/hackathons/{hackathon_id}/events/{event_id}")
    void getEvent(@Path("hackathon_id") int hackathon_id,
                  @Path("event_id") int event_id,
                  Callback<Event> callback);

    @POST("/hackathons/{hackathon_id}/events")
    void createEvent(@Header("Authorization") String token,
                     @Path("hackathon_id") int hackathon_id,
                     @Body Event event,
                     Callback<Event> callback);

    @PUT("/hackathons/{hackathon_id}/events/{event_id}")
    void updateEvent(@Header("Authorization") String token,
                     @Path("hackathon_id") int hackathon_id,
                     @Path("event_id") int event_id,
                     @Body Event event,
                     Callback<Event> callback);

    @DELETE("/hackathons/{hackathon_id}/events/{event_id}")
    void deleteEvent(@Header("Authorization") String token,
                     @Path("hackathon_id") int hackathon_id,
                     @Path("event_id") int event_id,
                     Callback<GenericResponse> callback);

    @GET("/hackathons/{hackathon_id}/locations")
    void getLocations(@Path("hackathon_id") int hackathon_id, Callback<List<Location>> callback);

    @POST("/hackathons/{hackathon_id}/locations")
    void createLocation(@Header("Authorization") String token,
                        @Path("hackathon_id") int hackathon_id,
                        @Body Location location,
                        Callback<Location> callback);

    @GET("/hackathons/{hackathon_id}/contacts")
    void getContacts(@Header("Authorization") String token,
                     @Path("hackathon_id") int hackathon_id,
                     Callback<List<User>> callback);

    @POST("/hacker_roles")
    void createHackerRole(@Header("Authorization") String token,
                          @Body HackerRole hackerRole,
                          Callback<HackerRole> callback);

    @PUT("/hacker_roles/{hacker_role_id}")
    void updateHackerRole(@Header("Authorization") String token,
                          @Path("hacker_role_id") int hacker_role_id,
                          @Body HackerRole hackerRole,
                          Callback<HackerRole> callback);

    @GET("/hackathons/{hackathon_id}/awards")
    void getAwards(@Path("hackathon_id") int hackathon_id, Callback<List<Award>> callback);

    @POST("/hackathons/{hackathon_id}/awards")
    void createAward(@Header("Authorization") String token,
                     @Path("hackathon_id") int hackathon_id,
                     @Body Award award,
                     Callback<Award> callback);
}
