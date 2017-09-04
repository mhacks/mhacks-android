package com.mhacks.android.data.network

import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.network.services.HackathonApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jeffreychang on 9/3/17.
 */
class NetworkSingleton private constructor(application: MHacksApplication) {

    @Inject lateinit var hackathonAPIService: HackathonApiService

    init {
        application.hackathonComponent.inject(this)
    }

    fun getConfiguration(callback: Callback<Config>) {
        hackathonAPIService.getConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { response ->
                        callback.success(response)
                    },
                    { error ->
                        callback.failure(error)

                    }
                )
    }

    companion object {
        fun newInstance(application: MHacksApplication): NetworkSingleton {
            return NetworkSingleton(application)
        }
    }

    interface Callback<T> {
        fun success(response: T)
        fun failure(error: Throwable)
    }
}