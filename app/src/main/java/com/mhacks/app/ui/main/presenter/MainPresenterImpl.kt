package com.mhacks.app.ui.main.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.main.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jeffreychang on 2/16/18.
 */

class MainPresenterImpl(val mainView: MainView,
                        var mHacksService: MHacksService,
                        var mHacksDatabase: MHacksDatabase): MainPresenter {

    override fun onCheckIfLoggedIn() {
        mHacksDatabase.loginDao().getLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mainView.onLogInSuccess(it) },
                        { mainView.onLogInFailure() })
    }
}