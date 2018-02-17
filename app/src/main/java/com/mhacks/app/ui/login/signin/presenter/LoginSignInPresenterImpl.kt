package com.mhacks.app.ui.login.signin.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.login.signin.view.LoginSignInView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jeffreychang on 2/16/18.
 */

class LoginSignInPresenterImpl(private val loginSignInView: LoginSignInView,
                               private val mHacksService: MHacksService,
                               private val mHacksDatabase: MHacksDatabase) :
        BasePresenterImpl(),
        LoginSignInPresenter {

    override fun postLogin(username: String, password: String) {
        compositeDisposable?.add(
                mHacksService.postLogin(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { loginSignInView.postLoginSuccess(it) },
                                { loginSignInView.postLoginFailure() }
                        )
        )
    }
}