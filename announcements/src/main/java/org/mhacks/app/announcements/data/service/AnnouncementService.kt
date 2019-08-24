package org.mhacks.app.announcements.data.service

import org.mhacks.app.announcements.data.model.AnnouncementResponse
import io.reactivex.Single
import retrofit2.http.GET

interface AnnouncementService {

    @GET("announcements")
    fun getAnnouncementResponse(): Single<AnnouncementResponse>

}