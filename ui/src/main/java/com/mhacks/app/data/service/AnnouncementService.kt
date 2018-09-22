package com.mhacks.app.data.service

import com.mhacks.app.data.models.AnnouncementResponse
import io.reactivex.Single
import retrofit2.http.GET

interface AnnouncementService {

    @GET("announcements")
    fun getAnnouncementResponse(): Single<AnnouncementResponse>

}