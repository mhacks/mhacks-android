package com.mhacks.app.ui.login.signin.presenter

import com.mhacks.app.ui.common.BasePresenter

/**
 * Created by jeffreychang on 2/16/18.
 */

interface LoginSignInPresenter: BasePresenter {

    fun postLogin(username: String, password: String)

    fun skipLogin()
}