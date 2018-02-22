package com.mhacks.app.di.module

import com.mhacks.app.ui.announcement.AnnouncementFragmentProvider
import com.mhacks.app.ui.announcement.createannouncement.CreateAnnouncementDialogFragmentProvider
import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementDialogFragment
import com.mhacks.app.ui.events.EventsFragmentProvider
import com.mhacks.app.ui.login.LoginActivity
import com.mhacks.app.ui.login.signin.LoginSignInFragmentProvider
import com.mhacks.app.ui.main.MainActivityModule
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.map.MapViewFragmentProvider
import com.mhacks.app.ui.qrscan.QRScanActivity
import com.mhacks.app.ui.qrscan.QRScanActivityModule
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
        AnnouncementFragmentProvider::class,
        CreateAnnouncementDialogFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        LoginSignInFragmentProvider::class
    ])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [
        QRScanActivityModule::class])
    abstract fun bindQRScanActivity(): QRScanActivity
}