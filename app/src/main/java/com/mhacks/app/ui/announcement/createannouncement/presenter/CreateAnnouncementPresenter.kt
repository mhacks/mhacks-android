package com.mhacks.app.ui.announcement.createannouncement.presenter

import com.mhacks.app.ui.common.BasePresenter

/**
 * Contract of presenter of the Create Announcement Fragment.
 */

interface CreateAnnouncementPresenter: BasePresenter {

    fun postAnnouncement()
}