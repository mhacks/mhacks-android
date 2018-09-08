package com.mhacks.app.ui.welcome.view

import com.mhacks.app.data.models.Configuration

interface WelcomeView {

    fun onGetConfigSuccess(config: Configuration)

    fun onGetConfigFailure(error: Throwable)
}