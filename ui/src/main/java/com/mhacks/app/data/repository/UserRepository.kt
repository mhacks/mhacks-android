package com.mhacks.app.data.repository

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.service.UserService
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userService: UserService,
        private val loginDao: LoginDao) {

    fun getLoginCache() = loginDao.getLogin()

    fun postLogin(loginRequest: Login.Request): Single<Login> {
        val (email, password) = loginRequest
        return userService.postLogin(email, password)
    }

    fun updateLoginCache(login: Login) =
            Single.fromCallable {
                loginDao.insertLogin(login)
                return@fromCallable login
            }!!

}

