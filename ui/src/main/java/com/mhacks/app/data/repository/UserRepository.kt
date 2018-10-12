package com.mhacks.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.User
import com.mhacks.app.data.network.fcm.RegistrationIntentService
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.room.dao.UserDao
import com.mhacks.app.data.service.UserService
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userService: UserService,
        private val loginDao: LoginDao,
        private val userDao: UserDao,
        private val appContext: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    fun getLoginCache() = loginDao.getLogin()

    fun getUserCache() = userDao.getUser()

    fun getUserRemote() =
            userService.getUserResponse()
                    .map {
                        it.user
                    }!!

    fun verifyUserTicket(email: String) =
            userService.verifyUserTicket(email)

    fun postLogin(loginRequest: Login.Request): Single<Login> {
        val (email, password) = loginRequest
        return userService.postLogin(email, password)
    }

    fun updateLoginCache(login: Login) =
            Single.fromCallable {
                loginDao.insertLogin(login)
                sharedPreferences
                        .edit()
                        .putString(RegistrationIntentService.AUTH_TOKEN, login.token)
                        .apply()
                return@fromCallable login
            }!!


    fun updateUserCache(user: User) =
            Single.fromCallable {
                userDao.insertUser(user)
                return@fromCallable user
            }!!


}

