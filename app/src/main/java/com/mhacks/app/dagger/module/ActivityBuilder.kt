package com.mhacks.app.dagger.module

import com.mhacks.app.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
//        SpecialBlendFragmentProvider::class,
//        MatchFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity
}