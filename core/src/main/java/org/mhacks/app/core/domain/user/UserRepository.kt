package org.mhacks.app.core.domain.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Single
import org.mhacks.app.core.domain.user.dao.UserDao
import org.mhacks.app.core.domain.user.data.User
import org.mhacks.app.core.domain.user.service.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userService: UserService,
        private val userDao: UserDao,
        private val appContext: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    fun getUserCache() = userDao.getUser()

    fun getUserRemote() =
            userService.getUserResponse()
                    .map {
                        it.user
                    }

    fun verifyUserTicket(email: String) =
            userService.verifyUserTicket(email)

    fun updateUserCache(user: User) =
            Single.fromCallable {
                userDao.insertUser(user)
                return@fromCallable user
            }
}