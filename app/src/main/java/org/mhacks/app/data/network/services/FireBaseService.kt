package org.mhacks.app.data.network.services

import io.reactivex.Single
import org.mhacks.app.data.model.FcmDevice
import retrofit2.http.*

/**
 * Serivce for posting FireBase Token.
 */

interface FireBaseService {

    @FormUrlEncoded
    @POST("device")
    fun postFireBaseToken(@Field("push_id") pushId: String, @Header("Authorization") authToken: String): Single<FcmDevice>

}
