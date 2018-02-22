package com.mhacks.app.ui.announcement.createannouncement

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.announcement.createannouncement.presenter.CreateAnnouncementPresenter
import com.mhacks.app.ui.announcement.createannouncement.presenter.CreateAnnouncementPresenterImpl
import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementDialogFragment
import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for announcement module.
 */
@Module
abstract class CreateAnnouncementDialogFragmentModule {

    @Binds
    abstract fun provideCreateAnnouncementView(
            createAnnouncementDialogFragment: CreateAnnouncementDialogFragment)
            : CreateAnnouncementView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideCreateAnnouncementPresenter(createAnnouncementView: CreateAnnouncementView,
                                               mHacksService: MHacksService,
                                               mHacksDatabase: MHacksDatabase): CreateAnnouncementPresenter =
                CreateAnnouncementPresenterImpl(createAnnouncementView, mHacksService, mHacksDatabase)
    }
}