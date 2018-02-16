package com.mhacks.app.ui.welcome.presenter

import com.mhacks.app.ui.welcome.view.WelcomeView

/**
 * Created by jawad on 04/11/14.
 * Updated by Shashank on 08/30/17
 */

class WelcomeFragmentPresenterImpl(private val welcomeView: WelcomeView): WelcomeFragmentPresenter {

    override fun onViewLoad() = welcomeView.onViewLoaded()

}