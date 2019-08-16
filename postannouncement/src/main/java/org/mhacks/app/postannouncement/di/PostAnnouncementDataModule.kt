package org.mhacks.app.postannouncement.di

import dagger.Module
import dagger.Provides
import org.mhacks.app.postannouncement.usecase.PostAnnouncementService
import org.mhacks.app.core.di.module.FeatureScope
import retrofit2.Retrofit

/**
 * Provides data dependencies for creating :postannouncement module.
 */
@Module
class PostAnnouncementDataModule {

    @Provides
    @FeatureScope
    fun providePostAnnouncementService(retrofit: Retrofit) =
            retrofit.create(PostAnnouncementService::class.java)!!

}
