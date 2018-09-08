package com.mhacks.app.ui.login.signin

import com.mhacks.app.ui.login.signin.view.LoginSignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jeffreychang on 2/16/18.
 */

@Module
abstract class LoginSignInFragmentProvider {

    @ContributesAndroidInjector(modules = [LoginSignInFragmentModule::class])
    abstract fun provideLoginSignInFragmentProvider(): LoginSignInFragment
}