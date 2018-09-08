package com.mhacks.app.ui.welcome

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.welcome.presenter.WelcomeFragmentPresenter
import com.mhacks.app.ui.welcome.presenter.WelcomeFragmentPresenterImpl
import com.mhacks.app.ui.welcome.view.WelcomeFragment
import com.mhacks.app.ui.welcome.view.WelcomeView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class WelcomeFragmentModule {

    @Binds
    abstract fun provideWelcomeView(welcomeFragment: WelcomeFragment): WelcomeView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideWelcomePresenter(welcomeView: WelcomeView,
                                    mHacksService: MHacksService,
                                    mHacksDatabase: MHacksDatabase)
                : WelcomeFragmentPresenter =
                WelcomeFragmentPresenterImpl(
                        welcomeView,
                        mHacksService,
                        mHacksDatabase)
    }
}