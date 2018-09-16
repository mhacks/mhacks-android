package com.mhacks.app.data.service

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.UserResponse
import com.mhacks.app.data.models.VerifyTicketResponse
import io.reactivex.Single
import retrofit2.http.*

interface UserService {

    @POST("auth/config/")
    @FormUrlEncoded
    fun postLogin(@Body loginRequest: Login.Request): Single<Login>

    @GET("user/profile/")
    fun getUserResponse(): Single<UserResponse>

    @POST("user/ticket/verify")
    @FormUrlEncoded
    fun verifyUserTicket(@Field("email") email: String): Single<VerifyTicketResponse>
}