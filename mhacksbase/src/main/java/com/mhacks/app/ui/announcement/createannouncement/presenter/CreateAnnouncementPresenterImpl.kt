package com.mhacks.app.ui.announcement.createannouncement.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementView
import com.mhacks.app.ui.common.BasePresenterImpl

/**
 * Implementation of the create announcement presenter.
 */
class CreateAnnouncementPresenterImpl(private val createAnnounementView: CreateAnnouncementView,
                                      private val mHacksService: MHacksService,
                                      private val mHacksDatabase: MHacksDatabase)
    : CreateAnnouncementPresenter, BasePresenterImpl() {

    override fun postAnnouncement() {

    }
}