package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.model.Quest
import javax.inject.Inject

class ScanQuestUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Quest, Quest>() {

    override fun getSingle(parameters: Quest): Single<Quest> {
        return gameRepository.scanQuest(parameters)
    }

}