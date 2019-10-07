package org.mhacks.app.game.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import java.util.concurrent.TimeUnit
import org.mhacks.app.game.data.model.Player
import org.mhacks.app.game.data.model.UserInfo
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, List<Player>>() {

    override fun getSingle(parameters: Unit): Single<List<Player>> =
            gameRepository
                    .getLeaderboardCache()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (it.isEmpty()) {
                            gameRepository.getLeaderboardRemote()
                        } else {
                            Single.just(it)
                        }
                    }
                    .doOnSuccess {
                        gameRepository.putLeaderboardCache(it)
                    }
}