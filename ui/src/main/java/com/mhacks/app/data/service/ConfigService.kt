package com.mhacks.app.data.service

import com.mhacks.app.data.models.ConfigResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigService {

    @GET("configuration/")
    fun getConfigResponse(): Single<ConfigResponse>

}