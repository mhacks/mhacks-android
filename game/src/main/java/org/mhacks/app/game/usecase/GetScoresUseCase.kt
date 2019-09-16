package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.data.model.Score
import javax.inject.Inject

class GetScoresUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, List<Score>>() {

    override fun getSingle(parameters: Unit): Single<List<Score>>{
        return gameRepository.getScores()
    }

}