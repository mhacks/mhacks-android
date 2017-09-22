package com.mhacks.android.ui.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import com.mhacks.android.data.kotlin.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import java.io.IOException


/**
 * Created by jeffreychang on 9/13/17.
 */
class NetworkUtil {

    companion object {
        fun checkIfNetworkSucceeds(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

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

                    success(BitmapFactory.decodeStream(response.body()!!.byteStream()))
                }

            });


        }
    }
}


//    public void getImage(String url, final HackathonCallback<Bitmap> callback) {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response)
//                    throws IOException {
//                Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
//
//                callback.success(image);
//            }
//        });
//    }
