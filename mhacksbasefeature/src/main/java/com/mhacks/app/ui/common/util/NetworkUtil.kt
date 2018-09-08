package com.mhacks.app.ui.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


/**
 * Utilities that have to do with networking.
 */
object NetworkUtil {

    fun getImage(url: String, success: (image: Bitmap) -> Unit,
                 failure: (error: Throwable) -> Unit) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(object: okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                failure(e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    error(IOException("Floor image response was not successful"))
                }
                success(BitmapFactory.decodeStream(response.body()?.byteStream()))
            }
        })
    }
}