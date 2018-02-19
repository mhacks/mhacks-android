package com.mhacks.app.data.network.services

import com.mhacks.app.data.kotlin.*
import com.mhacks.app.data.model.FcmDevice
import io.reactivex.Single
import retrofit2.http.*

/**
 * Provides definitions for interactions with the RESTful service.
 */

interface MHacksService {

    @GET("configuration/")
    fun getConfigurationResponse(): Single<ConfigurationResponse>

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                  @Field("password") password: String): Single<Login>

    @GET("user/profile/")
    fun getUserResponse(): Single<UserResponse>

    @GET("floor")
    fun getMetaFloors(): Single<FloorResponse>

    @GET("announcements")
    fun getAnnouncementResponse(): Single<AnnouncementResponse>

    @GET("event")
    fun getEventResponse(): Single<EventsResponse>

    @FormUrlEncoded
    @POST("device")
    fun postFirebaseToken(@Field("push_id") pushId: String): Single<FcmDevice>

}
