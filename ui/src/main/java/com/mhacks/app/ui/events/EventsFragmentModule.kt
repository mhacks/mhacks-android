package com.mhacks.app.ui.events

import androidx.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for the events module.
 */
@Module
abstract class EventsFragmentModule {


    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    abstract fun bindEventsViewModel(eventsViewModel: EventsViewModel): ViewModel
}