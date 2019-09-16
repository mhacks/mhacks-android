package org.mhacks.app.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.data.model.Score
import org.mhacks.app.game.usecase.GetQuestsUseCase
import org.mhacks.app.game.usecase.GetScoresUseCase
import org.mhacks.app.game.usecase.ScanQuestUseCase
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class GameViewModel @Inject constructor(
        private val getQuestsUseCase: GetQuestsUseCase,
        private val scanQuestUseCase: ScanQuestUseCase,
        private val getScoresUseCase: GetScoresUseCase
) : ViewModel() {

    private val getQuestsResult = getQuestsUseCase.observe()

    private val _questsLiveData = MediatorLiveData<List<Quest>>()
    val questLiveData get() = _questsLiveData

    private val _scoresLiveData = MediatorLiveData<List<Score>>()
    private val getScoresResult = getScoresUseCase.observe()
    val scoresLiveData get() = _scoresLiveData

    private val _snackBarMessage: MutableLiveData<Text> = MutableLiveData()
    val snackBarMessage: LiveData<Text> get() = _snackBarMessage

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    init {
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
                        }
                    }
                }
            }
        }
        _scoresLiveData.addSource(getScoresResult) {
            if (it is Outcome.Success) {
                it.let { scores ->
                    _scoresLiveData.value = scores.data
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
                        }
                    }
                }
            }
        }
        getQuestsUseCase(Unit)
        getScoresUseCase(Unit)
    }

    fun scanQuest(quest: Quest?) {
        if (quest != null) {
            scanQuestUseCase(quest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getQuestsUseCase.onCleared()
        scanQuestUseCase.onCleared()
        getScoresUseCase.onCleared()
    }

}