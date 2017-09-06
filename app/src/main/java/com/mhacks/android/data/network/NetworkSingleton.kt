package com.mhacks.android.data.network

import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.network.services.HackathonApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jeffreychang on 9/3/17.
 */
class NetworkSingleton (application: MHacksApplication) {

    @Inject lateinit var hackathonAPIService: HackathonApiService

    init {
        application.hackathonComponent.inject(this)
    }

    fun getConfiguration(
            success: (response: Config) -> Unit,
            failure: (failure: Throwable) -> Unit) {
        hackathonAPIService.getConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { response -> success(response) },
                    { error    -> failure(error) }
                )
    }

    fun getLoginVerification(email: String, password: String,
                             success: (response: Login) -> Unit,
                             failure: (failure: Throwable) -> Unit) {
        hackathonAPIService.getLogin(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { response -> success(response) },
                    { error    -> failure(error) }
                )
    }

    companion object {
        fun newInstance(application: MHacksApplication): NetworkSingleton {
            return NetworkSingleton(application)
        }
    }


}