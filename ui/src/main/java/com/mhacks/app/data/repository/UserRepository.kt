package com.mhacks.app.data.repository

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.User
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.room.dao.UserDao
import com.mhacks.app.data.service.UserService
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userService: UserService,
        private val loginDao: LoginDao,
        private val userDao: UserDao) {

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
                return@fromCallable login
            }!!


    fun updateUserCache(user: User) =
            Single.fromCallable {
                userDao.insertUser(user)
                return@fromCallable user
            }!!


}

