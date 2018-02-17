package com.mhacks.app.ui.ticket.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.ticket.view.TicketDialogView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jeffreychang on 2/16/18.
 */

class TicketDialogPresenterImpl @Inject constructor(
        private var ticketDialogView: TicketDialogView,
        private val mhacksService: MHacksService,
        private val mHacksDatabase: MHacksDatabase) : TicketDialogPresenter {

    override fun getUser() {
//        mHacksDatabase.userDao()
//                .getUser()
//                .onErrorResumeNext ({
//                    mHacksDatabase.loginDao().getLogin()
//                            .flatMap({ login ->
//                                mhacksService.getMetaUser()
//                                        .flatMap { user -> user.user) }
//                            })
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { ticketDialogView.onGetUserSuccess(it) },
//                        { ticketDialogView.onGetUserFailure(it) })
    }
}