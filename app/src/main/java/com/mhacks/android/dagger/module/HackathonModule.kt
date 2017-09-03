package com.mhacks.android.dagger.module

import com.mhacks.android.dagger.scope.UserScope
import com.mhacks.android.data.model.Login
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Singleton

/**
 * Created by jeffreychang on 9/3/17.
 */

@Module class HackathonModule {

    @Provides
    @UserScope
    internal fun provideHackathonApiInterface(retrofit: Retrofit): HackathonApiInterface {
        return retrofit.create(HackathonApiInterface::class.java)
    }

    internal interface HackathonApiInterface {
        @FormUrlEncoded
        @POST("login/")
        fun login(@Field("username") username: String,
                  @Field("password") password: String): Observable<Login>
    }
}