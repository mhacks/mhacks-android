package org.mhacks.app.game

import io.reactivex.Single
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.data.model.Score
import org.mhacks.app.game.data.service.GameService
import org.mhacks.app.game.data.service.Payload
import javax.inject.Inject

class GameRepository @Inject constructor(
        private val gameService: GameService
) {

    // TODO: Replace with retrofit service implementation.

    //    fun getQuests() = gameService.getQuests()
    fun getQuestions() = Single.just(
            Payload(
                    true,
                    listOf(
                            Quest("gksdfgjksdfglad;kfjgsdflkjg", 500),
                            Quest("kfjgsdflkjg", 300),
                            Quest("kfjgsdflkjg", 300),
                            Quest("kfjgsdflkjg", 300)
                    )
            ))

    // TODO: Don't know what this looks like
    fun scanQuest(quest: Quest): Single<Quest> {
        return Single.just(Quest("", 400))
//        return gameService.scanQuest(quest)
    }

    // TODO: Don't know what this looks like
//    fun getScores() : Single<List<Score>> {
//        return gameService.getScores()
//    }
    fun getScores(): Single<List<Score>> {
        return Single.just(
                listOf(
                        Score("jeffrey", 100),
                        Score("jeffrey2", 200),
                        Score("jeffrey3", 300)
                )
        )
    }

}