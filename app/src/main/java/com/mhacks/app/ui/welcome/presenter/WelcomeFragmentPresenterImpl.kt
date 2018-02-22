package com.mhacks.app.ui.welcome.presenter

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.common.BasePresenterImpl
import com.mhacks.app.ui.welcome.view.WelcomeView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jawad on 04/11/14.
 * Updated by Shashank on 08/30/17
 */

class WelcomeFragmentPresenterImpl(private val welcomeView: WelcomeView,
                                   private val mHacksService: MHacksService,
                                   private val mHacksDatabase: MHacksDatabase)
    : WelcomeFragmentPresenter, BasePresenterImpl() {

    override fun getConfig() {
        compositeDisposable?.add(
                mHacksDatabase.configDao().getConfig()
                        .onErrorResumeNext {
                            mHacksService.getConfigurationResponse()
                                    .map { it.configuration }
                        }
                        .doOnSuccess {
                            Observable.fromCallable {
                                mHacksDatabase.configDao().insertConfig(it)
                            }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            welcomeView.onGetConfigSuccess(it)
                        }, {
                            welcomeView.onGetConfigFailure(it)
                        })
        )
    }
}