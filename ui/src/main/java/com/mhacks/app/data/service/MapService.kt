package com.mhacks.app.data.service

import com.mhacks.app.data.models.FloorResponse
import io.reactivex.Single
import retrofit2.http.GET

interface MapService {

    @GET("floor")
    fun getFloorResponse(): Single<FloorResponse>

}