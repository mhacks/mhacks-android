package com.mhacks.app.ui.events

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.events.presenter.EventsFragmentPresenter
import com.mhacks.app.ui.events.presenter.EventsFragmentPresenterImpl
import com.mhacks.app.ui.events.view.EventsFragment
import com.mhacks.app.ui.events.view.EventsView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class EventsFragmentModule {

    @Binds
    abstract fun provideEventsView(eventsFragment: EventsFragment): EventsView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideEventsPresenter(eventsView: EventsView,
                                   mHacksService: MHacksService,
                                   mHacksDatabase: MHacksDatabase): EventsFragmentPresenter =
                EventsFragmentPresenterImpl(eventsView, mHacksService, mHacksDatabase)
    }
}