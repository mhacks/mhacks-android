package com.mhacks.app.ui.welcome

import android.arch.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import com.mhacks.app.ui.events.EventsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class WelcomeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    abstract fun bindEventsViewModel(eventsViewModel: EventsViewModel): ViewModel

}