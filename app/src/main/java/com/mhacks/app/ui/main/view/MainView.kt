package com.mhacks.app.ui.main.view

import com.mhacks.app.data.model.Login

/**
 * Created by jeffreychang on 2/16/18.
 */

interface MainView {

    fun onLogInSuccess(login: Login)

    fun onLogInFailure()

}