package com.mhacks.android.data.network.services

import com.mhacks.android.data.kotlin.Config
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by jeffreychang on 9/3/17.
 */
interface HackathonApiService {

    @GET("configuration/")
    fun getConfiguration(): Observable<Config>

//    @
//    fun getLogin(): Observable<Login>
}
