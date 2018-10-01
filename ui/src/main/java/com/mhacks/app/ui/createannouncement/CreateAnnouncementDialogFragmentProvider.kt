package com.mhacks.app.ui.createannouncement

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jeffreychang on 2/21/18.
 */

@Module
abstract class CreateAnnouncementDialogFragmentProvider {

    @ContributesAndroidInjector(modules = [CreateAnnouncementModule::class])
    abstract fun provideCreateAnnouncementDialogFragment(): CreateAnnouncementDialogFragment
}