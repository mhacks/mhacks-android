package org.mhacks.app.core.domain.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.reactivex.Single
import org.mhacks.app.core.Constants
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.core.domain.auth.data.service.AuthService

class AuthRepository(
        private val appContext: Context,
        private val authDao: AuthDao,
        private val authService: AuthService
) {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    fun getCachedAuth() = authDao.getAuth()

    fun deleteAuth() = Single.fromCallable { authDao.deleteAuth() }

    fun updateCachedAuth(auth: Auth) =
            Single.fromCallable {
                authDao.insertLogin(auth)
                sharedPreferences
                        .edit()
                        .putString(Constants.FIREBASE_AUTH_TOKEN, auth.token)
                        .apply()
                return@fromCallable auth
            }

    fun postAuth(email: String, password: String) = authService.postLogin(email, password)

}