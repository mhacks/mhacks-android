package org.mhacks.app.data.network.registration

import android.app.IntentService
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "RegIntentService"

/**
 * Service that receives tokens from FireBase and saved it into saved preferences.
 */
class RegistrationIntentService : IntentService(TAG) {

    @Inject
    lateinit var registrationRepository: RegistrationRepository

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    override fun onHandleIntent(intent: Intent?) {
        FirebaseInstanceId
                .getInstance()
                .instanceId.addOnSuccessListener {
            if (it != null) {
                Timber.d("Register to service")
                registrationRepository.persistAuthToken(it.token)
                sendRegistrationToServer(it.token)
            } else {
                Timber.d("Save to Shared Prefs that our value is false")
                registrationRepository.persistSentTokenToServer(false)
            }
        }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    private fun sendRegistrationToServer(token: String) {
        Timber.e(token)
        registrationRepository.postFirebaseToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { registrationRepository.persistSentTokenToServer(true) },
                        { Timber.e(it) }
                )
                .addTo(compositeDisposable)
    }

}