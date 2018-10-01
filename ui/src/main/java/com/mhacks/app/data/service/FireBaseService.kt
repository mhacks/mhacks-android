package com.mhacks.app.data.service

import com.mhacks.app.data.models.FcmDevice
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FireBaseService {

    @FormUrlEncoded
    @POST("device")
    fun postFireBaseToken(@Field("push_id") pushId: String): Single<FcmDevice>
}