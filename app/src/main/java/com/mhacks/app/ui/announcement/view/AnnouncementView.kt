package com.mhacks.app.ui.announcement.view

import com.mhacks.app.data.models.Announcement

/**
 * Created by jeffreychang on 2/16/18.
 */

interface AnnouncementView {

    fun onGetAnnouncementsSuccess(announcements: List<Announcement>)

    fun onGetAnnouncementsFailure(error: Throwable)
}
