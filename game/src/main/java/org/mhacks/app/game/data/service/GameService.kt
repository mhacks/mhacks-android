package org.mhacks.app.game.data.service

import io.reactivex.Single
import org.mhacks.app.game.data.model.Question
import retrofit2.http.GET

data class Payload(
        val status: String,
        val questions: Question
)

interface GameService {

    @GET("/questions")
    fun getQuestions(): Single<Payload>

}