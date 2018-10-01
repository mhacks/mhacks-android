package com.mhacks.app.ui.events

import com.mhacks.app.ui.events.widget.EventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides dependencies into special blend fragment.
 */
@Module
abstract class EventsFragmentProvider {

    @ContributesAndroidInjector(modules = [EventsFragmentModule::class])
    abstract fun provideEventsFragment(): EventsFragment
}