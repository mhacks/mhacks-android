package com.mhacks.app.ui.ticket

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.ticket.presenter.TicketDialogPresenter
import com.mhacks.app.ui.ticket.presenter.TicketDialogPresenterImpl
import com.mhacks.app.ui.ticket.view.TicketDialogFragment
import com.mhacks.app.ui.ticket.view.TicketDialogView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class TicketDialogModule {

    @Binds
    abstract fun provideTicketDialogView(ticketDialogFragment: TicketDialogFragment): TicketDialogView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideTicketDialogPresenter(ticketDialogView: TicketDialogView,
                                         mHacksService: MHacksService,
                                         mHacksDatabase: MHacksDatabase,
                                         authInterceptor: AuthModule.AuthInterceptor)
                : TicketDialogPresenter =
                TicketDialogPresenterImpl(
                        ticketDialogView,
                        mHacksService,
                        mHacksDatabase,
                        authInterceptor)
    }
}