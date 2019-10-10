package org.mhacks.app.game.data.service

import io.reactivex.Single
import org.mhacks.app.game.data.model.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GameService {

    @POST("game/scan/")
    @FormUrlEncoded
    fun scanQuest(@Field("email") email: String,
                  @Field("quest") quest: String) : Single<GameStateResponse>

    @GET("game/")
    fun getGameStateResponse(): Single<GameStateResponse>

    @GET("game/leaderboard/")
    fun getLeaderboardResponse(): Single<LeaderboardResponse>

    @GET("game/questions/")
    fun getQuestionResponse(): Single<QuestionResponse>
}