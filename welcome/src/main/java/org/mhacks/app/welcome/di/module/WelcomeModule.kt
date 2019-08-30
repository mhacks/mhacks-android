package org.mhacks.app.welcome.di.module

import androidx.lifecycle.ViewModel
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.welcome.WelcomeViewModel
import org.mhacks.app.eventlibrary.EventViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class WelcomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindEventViewModel(eventsViewModel: EventViewModel): ViewModel

}