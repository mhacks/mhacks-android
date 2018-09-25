package com.mhacks.app.ui.info

import android.arch.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for creating announcements module.
 */
@Module
abstract class InfoFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel::class)
    abstract fun bindInfoViewModel(
            createInfoViewModel: InfoViewModel): ViewModel

}
