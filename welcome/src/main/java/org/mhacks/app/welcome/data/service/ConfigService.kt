package org.mhacks.app.welcome.data.service

import org.mhacks.app.welcome.data.model.ConfigResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigService {

    @GET("configuration/")
    fun getConfigResponse(): Single<ConfigResponse>

}