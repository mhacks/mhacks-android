package com.mhacks.android.data.network

/**
 * Created by jeffreychang on 9/12/17.
 */

/*
    Solution designed by Kevin
    https://stackoverflow.com/a/40667193/5314716
*/

import io.reactivex.Observable
import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class ConnectivityInterceptor(isNetworkActive: Observable<Boolean>) : Interceptor {

    private var isNetworkActive: Boolean = false

    init {
        isNetworkActive.subscribe(
                { _isNetworkActive -> this.isNetworkActive = _isNetworkActive },
                { _error -> Timber.e("NetworkActive error " + _error.message) })
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isNetworkActive) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }
}

class NoConnectivityException : IOException() {
    override val message: String?
        get() = "No network available, please check your WiFi or Data connection";
}