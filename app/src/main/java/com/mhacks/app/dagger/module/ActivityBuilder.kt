package com.mhacks.app.dagger.module

import com.mhacks.app.ui.main.MainActivityModule
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.ticket.TicketDialogModule
import com.mhacks.app.ui.ticket.TicketDialogProvider
import com.mhacks.app.ui.welcome.WelcomeFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        WelcomeFragmentProvider::class,
        TicketDialogProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity
}