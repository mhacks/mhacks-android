package com.mhacks.app.data.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import io.reactivex.SingleObserver
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.IOException

/**
 * Service for getting a bitmap response from a URL.
 */
class GetImageFromUrlService {

    fun getImageFromUrl(url: String) = GetImageFromUrlSingle(url)

    /**
     * Single that allows you to get bitmap from an image url.
     */
    class GetImageFromUrlSingle(private val url: String): Single<Bitmap>() {

        override fun subscribeActual(observer: SingleObserver<in Bitmap>) {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                    .url(url)
                    .build()
            client.newCall(request).enqueue(object: okhttp3.Callback {

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        observer.onSuccess(
                                BitmapFactory.decodeStream(
                                        response.body()?.byteStream()))
                    }
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Timber.e(e)
                    observer.onError(e)
                }

            })
        }
    }
}
