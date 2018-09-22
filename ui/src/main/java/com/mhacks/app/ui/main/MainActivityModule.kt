package com.mhacks.app.ui.main

import android.arch.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module for [MainActivity]
 */
@Module
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

}
