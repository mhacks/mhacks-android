package com.mhacks.android.data.network.services

import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.kotlin.RetrofitUser
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.model.Token
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by jeffreychang on 9/3/17.
 */

interface HackathonApiService {

    @GET("configuration/")
    fun getConfiguration(): Observable<Config>

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                 @Field("password") password: String): Observable<Login>

    @GET("user/profile/")
    fun getUser(): Single<RetrofitUser>

    @FormUrlEncoded
    @POST("push_notifications/gcm/")
    fun getGcmToken(@Header("Authorization") authToken: String,
                     @Field("name") channelPrefs: String,
                     @Field("registration_id") regId: String,
                     @Field("active") active: Boolean?): Observable<Token>
}
