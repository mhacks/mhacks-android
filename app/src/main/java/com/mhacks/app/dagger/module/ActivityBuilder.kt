package com.mhacks.app.dagger.module

import com.mhacks.app.ui.announcement.AnnouncementFragmentProvider
import com.mhacks.app.ui.events.EventsFragmentProvider
import com.mhacks.app.ui.events.MapViewFragmentProvider
import com.mhacks.app.ui.main.MainActivityModule
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.ticket.TicketDialogProvider
import com.mhacks.app.ui.welcome.WelcomeFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        WelcomeFragmentProvider::class,
        EventsFragmentProvider::class,
        MapViewFragmentProvider::class,
        AnnouncementFragmentProvider::class,
        TicketDialogProvider::class,
        AnnouncementFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity
}