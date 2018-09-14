package com.mhacks.app.ui.main

import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.di.ViewModelKey
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.main.presenter.MainPresenterImpl
import com.mhacks.app.ui.main.view.MainActivity
import com.mhacks.app.ui.main.view.MainView
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Module for [MainActivity]
 */
@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainView

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

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
