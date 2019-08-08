package org.mhacks.app.events.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.events.EventViewModel
import javax.inject.Named
import javax.inject.Singleton

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