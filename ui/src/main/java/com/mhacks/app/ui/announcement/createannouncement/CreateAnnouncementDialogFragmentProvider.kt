package com.mhacks.app.ui.announcement.createannouncement

import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jeffreychang on 2/21/18.
 */

@Module
abstract class CreateAnnouncementDialogFragmentProvider {

    @ContributesAndroidInjector(modules = [CreateAnnouncementDialogFragmentModule::class])
    abstract fun provideCreateAnnouncementDialogFragment(): CreateAnnouncementDialogFragment
}