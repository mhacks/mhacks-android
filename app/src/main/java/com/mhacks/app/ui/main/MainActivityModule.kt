package com.mhacks.app.ui.main

import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.main.presenter.MainPresenterImpl
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.main.view.MainView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Created by jeffreychang on 2/16/18.
 */
@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainActivity(mainActivity: MainActivity): MainView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideMainPresenter(mainView: MainView,
                                 mHacksService: MHacksService,
                                 mHacksDatabase: MHacksDatabase): MainPresenter =
                MainPresenterImpl(mainView, mHacksService, mHacksDatabase)
    }

}
