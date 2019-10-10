package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.model.GameState
import javax.inject.Inject

class GetGameStateUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, GameState>() {

    override fun getSingle(parameters: Unit): Single<GameState> {
        val receivedGameState = gameRepository.getGameStateRemote().map { gameStateResponse -> gameStateResponse.state }
        return receivedGameState
    }
}