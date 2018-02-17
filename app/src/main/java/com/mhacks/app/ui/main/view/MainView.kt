package com.mhacks.app.ui.main.view

import com.mhacks.app.data.kotlin.LoginResponse

/**
 * Created by jeffreychang on 2/16/18.
 */

interface MainView {

    fun onLogInSuccess(login: LoginResponse)

    fun onLogInFailure()

}