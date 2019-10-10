package org.mhacks.app.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.game.data.model.*
import org.mhacks.app.game.usecase.GetGameStateUseCase
import org.mhacks.app.game.usecase.GetLeaderboardUseCase
import org.mhacks.app.game.usecase.ScanQuestUseCase
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class GameViewModel @Inject constructor(
        private val getGameStateUseCase: GetGameStateUseCase,
        private val scanQuestUseCase: ScanQuestUseCase,
        private val getLeaderboardUseCase: GetLeaderboardUseCase) : ViewModel() {


    private val _scanQuestLiveData = MediatorLiveData<GameState>()
    private val getScanQuestResult = scanQuestUseCase.observe()
    val scanQuestLiveData get() = _scanQuestLiveData

    private val _questsLiveData = MediatorLiveData<GameState>()
    private val getQuestsResult = getGameStateUseCase.observe()
    val questLiveData get() = _questsLiveData

    private val _leaderboardLiveData = MediatorLiveData<List<Player>>()
    private val getLeaderboardsResult = getLeaderboardUseCase.observe()
    val leaderboardLiveData get() = _leaderboardLiveData

    private val _snackBarMessage: MutableLiveData<Text> = MutableLiveData()
    val snackBarMessage: LiveData<Text> get() = _snackBarMessage

    private val _errorLiveData: MutableLiveData<RetrofitException.Kind> = MutableLiveData()
    val errorLiveData: LiveData<RetrofitException.Kind>
        get() = _errorLiveData

    init {
        _scanQuestLiveData.addSource(getScanQuestResult) {
            if (it is Outcome.Success) {
                it.let { gamestate ->
                    _scanQuestLiveData.value = gamestate.data
                     getGameStateUseCase.execute(Unit)
                }
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    Text.TextString(retrofitException.exception.toString())
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                            _errorLiveData.value = RetrofitException.Kind.UNAUTHORIZED
                        }
                    }
                }
            }
        }

        _questsLiveData.addSource(getQuestsResult) {
            if (it is Outcome.Success) {
                it.let { quests ->
                     _questsLiveData.value = quests.data
                }
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    Text.TextString(retrofitException.exception.toString())
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                            _errorLiveData.value = RetrofitException.Kind.UNAUTHORIZED
                        }
                    }
                }
            }
        }
        _leaderboardLiveData.addSource(getLeaderboardsResult) {
            if (it is Outcome.Success) {
                it.let { leaderboard ->
                    _leaderboardLiveData.value = leaderboard.data
                }
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    Text.TextString(retrofitException.exception.toString())
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                            _errorLiveData.value = RetrofitException.Kind.UNAUTHORIZED
                        }
                    }
                }
            }
        }


        getGameStateUseCase(Unit)
        getLeaderboardUseCase(Unit)
    }

    fun scanQuest(email:String, quest: String) {
         scanQuestUseCase.execute(PostScan(email, quest))
    }

    override fun onCleared() {
        super.onCleared()
        getGameStateUseCase.onCleared()
        scanQuestUseCase.onCleared()
        getLeaderboardUseCase.onCleared()
    }
}