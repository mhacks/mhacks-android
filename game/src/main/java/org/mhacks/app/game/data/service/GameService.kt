package org.mhacks.app.game.data.service

import io.reactivex.Single
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.data.model.Score
import retrofit2.http.GET
import retrofit2.http.POST

data class Payload(
        val status: Boolean,
        val quests: List<Quest>
)

interface GameService {

    @POST
    fun scanQuest(quest: Quest) : Single<Quest>

    @GET("/quests")
    fun getQuestions(): Single<Payload>

    @GET("/scores")
    fun getScores(): Single<List<Score>>

}