package com.mhacks.android.data.network.services

import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.model.Login
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by jeffreychang on 9/3/17.
 */

interface HackathonApiService {

    @GET("configuration/")
    fun getConfiguration(): Observable<Config>

    @POST("auth/login/")
    @FormUrlEncoded
    fun getLogin(@Field("email") email: String,
                 @Field("password") password: String): Observable<Login>
}
