package com.mhacks.app.data.service

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.UserResponse
import com.mhacks.app.data.models.VerifyTicketResponse
import io.reactivex.Single
import retrofit2.http.*
import retrofit2.http.POST



interface UserService {

//    @POST("auth/config/")
//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    fun postLogin(@Body loginRequest: Login.Request): Single<Login>

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                  @Field("password") password: String): Single<Login>


    @GET("user/profile/")
    fun getUserResponse(): Single<UserResponse>

    @POST("user/ticket/verify")
    @FormUrlEncoded
    fun verifyUserTicket(@Field("email") email: String): Single<VerifyTicketResponse>
}