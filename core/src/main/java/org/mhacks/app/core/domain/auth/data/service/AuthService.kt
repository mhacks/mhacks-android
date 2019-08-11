package org.mhacks.app.core.domain.auth.data.service

import io.reactivex.Single
import org.mhacks.app.core.domain.auth.data.model.Auth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login/")
    @FormUrlEncoded
    fun postLogin(@Field("email") email: String,
                  @Field("password") password: String): Single<Auth>

}