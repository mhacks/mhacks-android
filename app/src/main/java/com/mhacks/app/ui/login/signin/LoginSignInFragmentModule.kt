package com.mhacks.app.ui.login.signin

import com.mhacks.app.ui.login.signin.presenter.LoginSignInPresenter
import com.mhacks.app.ui.login.signin.presenter.LoginSignInPresenterImpl
import com.mhacks.app.ui.login.signin.view.LoginSignInFragment
import com.mhacks.app.ui.login.signin.view.LoginSignInView
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Created by jeffreychang on 2/16/18.
 */

@Module
abstract class LoginSignInFragmentModule {

    @Binds
    abstract fun provideLoginSignInView(loginSignInFragment: LoginSignInFragment): LoginSignInView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideLoginSignInPresenter(loginSignInView: LoginSignInView): LoginSignInPresenter =
                LoginSignInPresenterImpl(loginSignInView)
    }
}