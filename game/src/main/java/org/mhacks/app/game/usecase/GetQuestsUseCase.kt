package org.mhacks.app.game.usecase

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.game.GameRepository
import org.mhacks.app.game.data.model.GameState
import org.mhacks.app.game.data.model.Question
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetGameStateUseCase @Inject constructor(
        private val gameRepository: GameRepository
) : SingleUseCase<Unit, GameState>() {

    override fun getSingle(parameters: Unit): Single<GameState> {
        val receivedGameState = gameRepository.getGameStateCache()
                .delay(400, TimeUnit.MILLISECONDS)
                .onErrorResumeNext {
                    gameRepository.getGameStateRemote().map { it }
                }.doOnSuccess {
                    gameRepository.putGameStateCache(it)
                }

        val receivedQuestions = gameRepository.getQuestionsCache()
                .delay(400, TimeUnit.MILLISECONDS)
                .flatMap {
                    if (it.isEmpty()) {
                        gameRepository.getQuestionsRemote()
                    } else {
                        Single.just(it)
                    }
                }.doOnSuccess{
                    gameRepository.putQuestionsCache(it)
                }

        return Single.zip(
                receivedGameState,
                receivedQuestions,
                BiFunction<GameState, List<Question>, GameState> {
                    gameState, questionList -> handler(gameState, questionList)
                })
    }

    private fun handler(gameState: GameState, questions: List<Question> ) : GameState {
        val questionTasks = mutableMapOf<String, String>()

        for (question in questions) {
            questionTasks[question.name] = question.taskFormat
        }

        val mutableQuests = gameState.quests.toMutableList()
        val questIterator = mutableQuests.listIterator()

        while (questIterator.hasNext()) {
            val currentQuest = questIterator.next()
            val task = questionTasks[currentQuest.question]
            val answer = currentQuest.answer

            if (task != null) {
                currentQuest.questionText = task.replace("{}", answer)
            }
            questIterator.set(currentQuest)
        }

        gameState.quests = mutableQuests.toList()
        return gameState
    }

}