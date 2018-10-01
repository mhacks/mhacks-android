package com.mhacks.app.ui.announcement

import androidx.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for announcement module.
 */

@Module
abstract class AnnouncementFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(AnnouncementViewModel::class)
    abstract fun bindAnnouncementViewModule(announcementViewModel: AnnouncementViewModel): ViewModel

}