package com.mhacks.app.ui.main.presenter

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.main.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the MainActivity presenter.
 */

class MainPresenterImpl(private val mainView: MainView,
                        private var mHacksDatabase: MHacksDatabase,
                        private val sharedPreferencesManager: SharedPreferencesManager)
    : MainPresenter {

    override fun checkAdmin() {
        mainView.onCheckAdmin(sharedPreferencesManager.getIsAdmin())
    }

    override fun checkIfLoggedIn() {
        mHacksDatabase.loginDao().getLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mainView.onLogInSuccess(it) },
                        { mainView.onLogInFailure() })
    }
}