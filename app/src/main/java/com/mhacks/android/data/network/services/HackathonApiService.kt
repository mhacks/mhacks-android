package com.mhacks.android.data.network.services

import com.mhacks.android.data.kotlin.MetaConfiguration
import com.mhacks.android.data.kotlin.MetaUser
import com.mhacks.android.data.model.FcmDevice
import com.mhacks.android.data.model.Login
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by jeffreychang on 9/3/17.
 */

interface HackathonApiService {

    @GET("configuration")
    fun getConfiguration(): Observable<MetaConfiguration>

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                  @Field("password") password: String): Observable<Login>

    @GET("user/profile/")
    fun getUser(): Single<MetaUser>
//
//    @GET("floor")
//    fun getFloors(@Field("push_id") pushId: String): Observable<MetaFloor>
//
//    @GET("announcement")
//    fun getFloors(@Field("push_id") pushId: String): Observable<MetaFloor>

    @FormUrlEncoded
    @POST("device")
    fun postFirebaseToken(@Field("push_id") pushId: String): Observable<FcmDevice>

}
