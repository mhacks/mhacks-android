package org.mhacks.app.core.domain.user.service

import io.reactivex.Single
import org.mhacks.app.core.domain.user.data.UserResponse
import org.mhacks.app.core.domain.user.data.VerifyTicket
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    
    @GET("user/profile/")
    fun getUserResponse(): Single<UserResponse>

    @POST("user/ticket/verify")
    @FormUrlEncoded
    fun verifyUserTicket(@Field("email") email: String): Single<VerifyTicket>

}