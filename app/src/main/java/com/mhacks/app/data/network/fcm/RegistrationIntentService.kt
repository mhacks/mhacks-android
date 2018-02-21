package com.mhacks.app.data.network.fcm

import android.app.IntentService
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import java.io.IOException
import android.preference.PreferenceManager
import android.content.SharedPreferences
import com.mhacks.app.data.network.services.MHacksService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Service that receives tokens from FireBase and saved it into saved preferences.
 */
class RegistrationIntentService : IntentService(TAG) {

    private val retrofit = Retrofit.Builder()
            .baseUrl("https://mhacks.org/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    private val mhacksService = retrofit.create(MHacksService::class.java)

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val instanceID = FirebaseInstanceId.getInstance()
        try {
            val token = instanceID.token
            if (token !== null) {
                sendRegistrationToServer(token)
            }
        } catch (e: IOException) {
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private fun sendRegistrationToServer(token: String) {
        Timber.e(token)
        mhacksService.postFireBaseToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply() },
                        { Timber.e(it) }
                )
    }

    companion object {
        private const val TAG = "RegIntentService"
        private const val SENT_TOKEN_TO_SERVER = "sentTokenToServer"
    }
}