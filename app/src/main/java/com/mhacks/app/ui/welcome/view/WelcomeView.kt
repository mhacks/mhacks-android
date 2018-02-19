package com.mhacks.app.ui.welcome.view

import com.mhacks.app.data.kotlin.Configuration

interface WelcomeView {

    fun onGetConfigSuccess(config: Configuration)

    fun onGetConfigFailure(error: Throwable)

}