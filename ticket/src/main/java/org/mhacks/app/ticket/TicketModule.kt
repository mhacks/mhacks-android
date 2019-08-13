package org.mhacks.app.ticket

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.ticket.TicketViewModel

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class TicketModule {

    @Binds
    @IntoMap
    @ViewModelKey(TicketViewModel::class)
    abstract fun bindTicketViewModel(ticketViewModel: TicketViewModel): ViewModel

}