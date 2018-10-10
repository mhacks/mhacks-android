package com.mhacks.app.data.network.services

import com.mhacks.app.data.models.FcmDevice
import io.reactivex.Single
import retrofit2.http.*

/**
 * Serivce for posting FireBase Token.
 */

interface FireBaseService {

    @FormUrlEncoded
    @POST("device")
    fun postFireBaseToken(@Field("push_id") pushId: String): Single<FcmDevice>

}
