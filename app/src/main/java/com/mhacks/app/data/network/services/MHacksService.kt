package com.mhacks.app.data.network.services

import com.mhacks.app.data.kotlin.*
import com.mhacks.app.data.model.FcmDevice
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by jeffreychang on 9/3/17.
 */

interface MHacksService {

    @GET("configuration")
    fun getMetaConfiguration(): Single<MetaConfiguration>

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                  @Field("password") password: String): Single<LoginResponse>

    @GET("user/profile/")
    fun getUserResponse(): Single<UserResponse>

    @GET("floor")
    fun getMetaFloors(): Single<MetaFloor>

    @GET("announcements")
    fun getMetaAnnouncements(): Single<MetaAnnouncements>

    @GET("event")
    fun getMetaEvent(): Single<MetaEvents>

    @FormUrlEncoded
    @POST("device")
    fun postFirebaseToken(@Field("push_id") pushId: String): Single<FcmDevice>

}
