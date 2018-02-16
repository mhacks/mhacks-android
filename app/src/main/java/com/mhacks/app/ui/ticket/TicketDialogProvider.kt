package com.mhacks.app.ui.ticket

import com.mhacks.app.ui.ticket.view.TicketDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides dependencies into special blend fragment.
 */
@Module
abstract class TicketDialogProvider {

    @ContributesAndroidInjector(modules = [TicketDialogModule::class])
    abstract fun provideTicketDialogFragment(): TicketDialogFragment
}