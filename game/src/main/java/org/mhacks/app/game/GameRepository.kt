package org.mhacks.app.game

import org.mhacks.app.game.data.service.GameService
import javax.inject.Inject

class GameRepository @Inject constructor(
        private val gameService: GameService
) {

    fun getQuestions() = gameService.getQuestions()

}