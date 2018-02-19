package com.mhacks.app.ui.login.signin.view

import com.mhacks.app.data.kotlin.Login

/**
 * Created by jeffreychang on 2/16/18.
 */
interface LoginSignInView {

    fun postLoginSuccess(login: Login)

    fun postLoginFailure(username: String, password: String, error: Throwable)

    fun skipLoginSuccess()
}