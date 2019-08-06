package org.mhacks.app.events.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.events.EventViewModel

/**
 * Provides dependencies for the events module.
 */
@Module
abstract class EventModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindEventViewModel(eventViewModel: EventViewModel): ViewModel

}