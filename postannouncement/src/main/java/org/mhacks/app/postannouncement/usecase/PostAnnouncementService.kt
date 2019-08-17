package org.mhacks.app.postannouncement.usecase

import io.reactivex.Single
import org.mhacks.app.postannouncement.data.model.PostAnnouncement
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PostAnnouncementService {

    @POST("announcements")
    @FormUrlEncoded
    fun postAnnouncement(@Field("title") title: String,
                         @Field("category") category: String,
                         @Field("body") body: String,
                         @Field("isApproved") isApproved: Boolean,
                         @Field("isSent") isSent: Boolean,
                         @Field("push") push: Boolean): Single<PostAnnouncement>

}