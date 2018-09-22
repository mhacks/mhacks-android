package com.mhacks.app.ui.ticket

import android.arch.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class TicketDialogModule {

    @Binds
    @IntoMap
    @ViewModelKey(TicketViewModel::class)
    abstract fun bindTicketViewModel(ticketViewModel: TicketViewModel): ViewModel

}