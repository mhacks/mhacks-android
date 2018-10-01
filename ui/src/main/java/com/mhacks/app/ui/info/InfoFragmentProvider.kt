package com.mhacks.app.ui.info

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by jeffreychang on 2/21/18.
 */

@Module
abstract class InfoFragmentProvider {

    @ContributesAndroidInjector(modules = [InfoFragmentModule::class])
    abstract fun provideInfoFragment(): InfoFragment

}