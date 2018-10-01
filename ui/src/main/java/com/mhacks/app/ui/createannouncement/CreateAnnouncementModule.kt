package com.mhacks.app.ui.createannouncement

import androidx.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for creating announcements module.
 */
@Module
abstract class CreateAnnouncementModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateAnnouncementViewModel::class)
    abstract fun bindCreateAnnouncementViewModel(
            createAnnouncementViewModel: CreateAnnouncementViewModel): ViewModel

}
