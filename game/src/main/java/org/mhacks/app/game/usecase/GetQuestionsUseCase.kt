package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.service.Payload
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, Payload>() {

    override fun getSingle(parameters: Unit): Single<Payload> {
        return gameRepository.getQuestions()
    }

}