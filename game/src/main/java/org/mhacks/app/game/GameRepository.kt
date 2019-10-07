package org.mhacks.app.game

import io.reactivex.Single
import org.mhacks.app.game.data.db.GameStateDao
import org.mhacks.app.game.data.db.LeaderboardDao
import org.mhacks.app.game.data.db.QuestionDao
import org.mhacks.app.game.data.model.*
import org.mhacks.app.game.data.service.GameService
import javax.inject.Inject

class GameRepository @Inject constructor(
        private val gameService: GameService,
        private val gamestateDao: GameStateDao,
        private val leaderboardDao: LeaderboardDao,
        private val questionDao: QuestionDao
) {

    fun getGameStateRemote() = gameService.getGameState().map { it.state }

    fun getGameStateCache() = gamestateDao.getGameState().map { it }

    fun putGameStateCache(gamestate: GameState) =
            Single.fromCallable {
                gamestateDao.updateGameState(gamestate)
                return@fromCallable gamestate
            }

    fun getLeaderboardRemote() = gameService.getLeaderboard().map { it.leaderboard }

    fun getLeaderboardCache() = leaderboardDao.getLeaderboard()

    fun putLeaderboardCache(leaderboard: List<Player>) =
            Single.fromCallable {
                leaderboardDao.updateLeaderboard(leaderboard)
                return@fromCallable leaderboard
            }

    fun scanQuest(postScan: PostScan) = gameService.scanQuest(postScan)

    fun getQuestionsRemote() = gameService.getQuestions().map { it.questions }

    fun getQuestionsCache() = questionDao.getQuestions()

    fun putQuestionsCache(questions: List<Question>) =
            Single.fromCallable {
                questionDao.updateQuestions(questions)
                return@fromCallable questions
            }
}