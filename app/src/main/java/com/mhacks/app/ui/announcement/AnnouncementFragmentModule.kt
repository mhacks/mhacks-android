package com.mhacks.app.ui.announcement

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.announcement.presenter.AnnouncementPresenter
import com.mhacks.app.ui.announcement.presenter.AnnouncementPresenterImpl
import com.mhacks.app.ui.announcement.view.AnnouncementFragment
import com.mhacks.app.ui.announcement.view.AnnouncementView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for announcement module.
 */
@Module
abstract class AnnouncementFragmentModule {

    @Binds
    abstract fun provideAnnouncementView(announcementFragment: AnnouncementFragment): AnnouncementView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideAnnouncementPresenter(announcementView: AnnouncementView,
                                         mHacksService: MHacksService,
                                         mHacksDatabase: MHacksDatabase): AnnouncementPresenter =
                AnnouncementPresenterImpl(announcementView, mHacksService, mHacksDatabase)
    }
}