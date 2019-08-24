package org.mhacks.app.announcements.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.announcements.data.db.AnnouncementDatabase
import org.mhacks.app.announcements.data.service.AnnouncementService
import retrofit2.Retrofit

@Module
class AnnouncementDataModule {

    @Provides
    @FeatureScope
    fun provideAnnouncementService(retrofit: Retrofit) : AnnouncementService =
            retrofit.create(AnnouncementService::class.java)

    @Provides
    @FeatureScope
    fun provideAnnouncementDatabase(context: Context) = AnnouncementDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideAnnouncementDao(announcementDatabase: AnnouncementDatabase) = announcementDatabase.announcementDao()

}