package com.mhacks.app.ui.announcement

import com.mhacks.app.ui.announcement.view.AnnouncementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides dependencies into special blend fragment.
 */
@Module
abstract class AnnouncementFragmentProvider {

    @ContributesAndroidInjector(modules = [AnnouncementFragmentModule::class])
    abstract fun provideAnnouncementFragment(): AnnouncementFragment
}