package org.mhacks.app.game.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.game.data.service.GameService
import retrofit2.Retrofit

@Module
class GameDataModule {

    @Provides
    @FeatureScope
    fun provideGameService(retrofit: Retrofit): GameService =
            retrofit.create(GameService::class.java)

}