package com.mhacks.app.ui.main.presenter

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.ui.main.view.MainView

/**
 * Created by jeffreychang on 2/16/18.
 */

class MainPresenterImpl(val mainView: MainView,
                        var mHacksService: MHacksService): MainPresenter {

    override fun onViewLoaded() {
        mainView.onViewLoaded()

    }
}