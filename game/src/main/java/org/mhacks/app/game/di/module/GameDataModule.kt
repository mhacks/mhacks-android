package org.mhacks.app.game.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.game.data.db.GameStateDatabase
import org.mhacks.app.game.data.db.LeaderboardDatabase
import org.mhacks.app.game.data.db.QuestionDatabase
import org.mhacks.app.game.data.service.GameService
import retrofit2.Retrofit

@Module
class GameDataModule {

    @Provides
    @FeatureScope
    fun provideGameService(retrofit: Retrofit): GameService =
            retrofit.create(GameService::class.java)

    @Provides
    @FeatureScope
    fun provideGameStateDatabase(context: Context) = GameStateDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideGameStateDao(gameStateDatabase: GameStateDatabase) = gameStateDatabase.gameStateDao()

    @Provides
    @FeatureScope
    fun provideLeaderboardDatabase(context: Context) = LeaderboardDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideLeaderboardDao(leaderboardDatabase: LeaderboardDatabase) = leaderboardDatabase.leaderboardDao()

    @Provides
    @FeatureScope
    fun provideQuestionDatabase(context: Context) = QuestionDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideQuestionDao(questionDatabase: QuestionDatabase) = questionDatabase.questionDao()

}