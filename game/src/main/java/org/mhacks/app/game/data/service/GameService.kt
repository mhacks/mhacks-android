package org.mhacks.app.game.data.service

import io.reactivex.Single
import org.mhacks.app.game.data.model.*
import retrofit2.http.GET
import retrofit2.http.POST

interface GameService {

    @POST("game/scan/")
    fun scanQuest(postScan: PostScan) : Single<GameStateResponse>

    @GET("game/")
    fun getGameState(): Single<GameStateResponse>

    @GET("game/leaderboard/")
    fun getLeaderboard(): Single<LeaderboardResponse>

    @GET("game/questions/")
    fun getQuestions(): Single<QuestionResponse>
}