package com.mhacks.app.data.repository

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.service.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userService: UserService,
        private val loginDao: LoginDao,
        private val sharedPreferencesManager: SharedPreferencesManager) {

    fun getLoginCache() = loginDao.getLogin()

    fun getLoginRemote(loginRequest: Login.Request) =
        userService.postLogin(loginRequest)

    fun getIsAdmin() = sharedPreferencesManager.getIsAdminRx()

}