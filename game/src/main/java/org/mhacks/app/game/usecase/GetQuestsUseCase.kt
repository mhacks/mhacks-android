package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.model.Quest
import javax.inject.Inject

class GetQuestsUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, List<Quest>>() {

    override fun getSingle(parameters: Unit): Single<List<Quest>> {
        return gameRepository.getQuestions().map { it.quests }
    }

}