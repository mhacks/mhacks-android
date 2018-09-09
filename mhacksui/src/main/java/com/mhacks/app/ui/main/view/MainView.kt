package com.mhacks.app.ui.main.view

import com.mhacks.app.data.models.Login

/**
 * View contract for the main activity.
 */

interface MainView {

    fun onLogInSuccess(login: Login)

    fun onLogInFailure()

    fun onCheckAdmin(isAdmin: Boolean)
}