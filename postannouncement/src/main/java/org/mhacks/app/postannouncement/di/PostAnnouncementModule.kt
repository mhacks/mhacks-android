package org.mhacks.app.postannouncement.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.postannouncement.PostAnnouncementViewModel
import org.mhacks.app.core.di.ViewModelKey

/**
 * Provides dependencies for creating :postannouncement module.
 */
@Module
abstract class PostAnnouncementModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostAnnouncementViewModel::class)
    abstract fun bindPostAnnouncementViewModel(
            postAnnouncementViewModel: PostAnnouncementViewModel): ViewModel

}
