package com.mhacks.android.data.network.fcm

import android.app.IntentService
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import com.mhacks.android.data.network.services.HackathonApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.android.R
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import android.preference.PreferenceManager
import android.content.SharedPreferences


/**
 * Created by jeffreychang on 9/19/17.
 */

class RegistrationIntentService : IntentService(TAG) {

    private val SENT_TOKEN_TO_SERVER = "sentTokenToServer"
    private val FCM_TOKEN = "FCMToken"


    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://staging.mhacks.org/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val hackathonService: HackathonApiService = retrofit.create(HackathonApiService::class.java)

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val instanceID = FirebaseInstanceId.getInstance()
        val senderId = resources.getString(R.string.gcm_defaultSenderId)
//        try {
//            val token = instanceID.token!!
//            sendRegistrationToServer(token)
//        } catch (e: IOException) {
//            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
//        }

    }

    private fun sendRegistrationToServer(token: String) {
        hackathonService.postFirebaseToken(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply() },
                        { failure -> Timber.e(failure) }
                )
    }

    companion object {

        private val TAG = "RegIntentService"
    }
}