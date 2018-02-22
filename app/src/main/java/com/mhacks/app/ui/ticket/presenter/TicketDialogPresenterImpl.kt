package com.mhacks.app.ui.ticket.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.ticket.view.TicketDialogView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of presenter for the ticket dialog.
 */

class TicketDialogPresenterImpl @Inject constructor(
        private var ticketDialogView: TicketDialogView,
        private val mHacksService: MHacksService,
        private val mHacksDatabase: MHacksDatabase,
        private val authInterceptor: AuthModule.AuthInterceptor) : TicketDialogPresenter {

    override fun getUser() {
        mHacksDatabase.userDao()
                .getUser()
                .onErrorResumeNext {
                    mHacksDatabase.loginDao().getLogin()
                            .flatMap {
                                authInterceptor.token = it.token
                                mHacksService.getUserResponse()
                            }
                            .flatMap {
                                Single.just(it.user) }
                }
                .doOnSuccess {
                    Observable.fromCallable {
                        mHacksDatabase.userDao().insertUser(it)
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { ticketDialogView.onGetUserSuccess(it) },
                        { ticketDialogView.onGetUserFailure(it) }
                )
    }
}