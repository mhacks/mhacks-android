package com.mhacks.app.ui.login.signin

import com.mhacks.app.data.SharedPreferencesManager
import com.mhacks.app.data.network.services.MHacksService
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.ui.login.signin.presenter.LoginSignInPresenter
import com.mhacks.app.ui.login.signin.presenter.LoginSignInPresenterImpl
import com.mhacks.app.ui.login.signin.view.LoginSignInFragment
import com.mhacks.app.ui.login.signin.view.LoginSignInView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Module that provides dependencies needed for login sign-in.
 */

@Module
abstract class LoginSignInFragmentModule {

    @Binds
    abstract fun provideLoginSignInView(loginSignInFragment: LoginSignInFragment): LoginSignInView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideLoginSignInPresenter(loginSignInView: LoginSignInView,
                                        mHacksService: MHacksService,
                                        mHacksDatabase: MHacksDatabase,
                                        sharedPreferencesManager: SharedPreferencesManager)
                : LoginSignInPresenter =
                LoginSignInPresenterImpl(
                        loginSignInView,
                        mHacksService,
                        mHacksDatabase, sharedPreferencesManager)
    }
}