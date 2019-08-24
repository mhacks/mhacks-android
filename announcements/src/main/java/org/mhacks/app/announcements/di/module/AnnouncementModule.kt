package org.mhacks.app.announcements.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import org.mhacks.app.core.di.ViewModelKey
import dagger.multibindings.IntoMap
import org.mhacks.app.announcements.AnnouncementViewModel

/**
 * Provides dependencies for announcement module.
 */

@Module
abstract class AnnouncementModule {

    @Binds
    @IntoMap
    @ViewModelKey(AnnouncementViewModel::class)
    abstract fun bindAnnouncementViewModule(announcementViewModel: AnnouncementViewModel): ViewModel

}