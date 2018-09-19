package com.mhacks.app.ui.signin

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jeffreychang on 2/16/18.
 */

@Module
abstract class SignInFragmentProvider {

    @ContributesAndroidInjector(modules = [SignInFragmentModule::class])
    abstract fun provideLoginSignInFragmentProvider(): SignInFragment

}