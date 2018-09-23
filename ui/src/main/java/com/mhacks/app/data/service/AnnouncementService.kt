package com.mhacks.app.data.service

import com.mhacks.app.data.models.AnnouncementResponse
import com.mhacks.app.data.models.CreateAnnouncement
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnnouncementService {

    @GET("announcements")
    fun getAnnouncementResponse(): Single<AnnouncementResponse>

    @POST("announcements")
    @FormUrlEncoded
    fun postAnnouncement(@Field("title") title: String,
                         @Field("category") category: String,
                         @Field("body") body: String,
                         @Field("isApproved") isApproved: Boolean,
                         @Field("isSent") isSent: Boolean,
                         @Field("push") push: Boolean): Single<CreateAnnouncement>

}