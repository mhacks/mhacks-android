package com.mhacks.app.ui.login.signin.presenter

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.login.signin.view.LoginSignInView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of Login Signin Presenter.
 */
class LoginSignInPresenterImpl(private val loginSignInView: LoginSignInView,
                               private val mHacksService: MHacksService,
                               private val mHacksDatabase: MHacksDatabase,
                               private val sharedPreferencesManager: SharedPreferencesManager) :
        BasePresenterImpl(),
        LoginSignInPresenter {

    override fun postLogin(username: String, password: String) {
        compositeDisposable?.add(
                mHacksService.postLogin(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess({
                            it.id = 1
                            Observable.fromCallable {
                                mHacksDatabase.loginDao().insertLogin(it)
                            }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                            if (it.user?.groups?.contains("admin")!!)
                                sharedPreferencesManager.putIsAdmin(true)
                        })
                        .subscribe(
                                { loginSignInView.postLoginSuccess(it) },
                                { loginSignInView.postLoginFailure(username, password, it) }
                        )
        )
    }

    override fun skipLogin() {
        Observable.fromCallable {
            mHacksDatabase.loginDao()
                    .insertLogin(Login(1,  false, "", ""))
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { loginSignInView.skipLoginSuccess() }
                .subscribe()

    }
}