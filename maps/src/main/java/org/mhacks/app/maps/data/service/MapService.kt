package org.mhacks.app.maps.data.service

import io.reactivex.Single
import org.mhacks.app.maps.data.model.FloorResponse
import retrofit2.http.GET

interface MapService {

    @GET("floor")
    fun getFloorResponse(): Single<FloorResponse>

}