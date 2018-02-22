package com.mhacks.app.ui.main

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.main.presenter.MainPresenterImpl
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.main.view.MainView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Module for [MainActivity]
 */
@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideMainPresenter(mainView: MainView,
                                 mHacksDatabase: MHacksDatabase,
                                 sharedPreferencesManager: SharedPreferencesManager)
                : MainPresenter =
                MainPresenterImpl(mainView, mHacksDatabase, sharedPreferencesManager)
    }
}
