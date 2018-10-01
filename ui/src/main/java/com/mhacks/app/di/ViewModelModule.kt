package com.mhacks.app.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: MHacksViewModelFactory):
            ViewModelProvider.Factory
}
