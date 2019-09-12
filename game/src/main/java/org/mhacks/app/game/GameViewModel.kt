package org.mhacks.app.game

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.game.data.service.Payload
import org.mhacks.app.game.usecase.GetQuestionsUseCase
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class GameViewModel @Inject constructor(
        private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val getQuestionsResult = getQuestionsUseCase.observe()

    private val _questionsLiveData = MediatorLiveData<Payload>()
    val questionsLiveData get() = _questionsLiveData

    private val _snackBarMessage: MutableLiveData<Text> = MutableLiveData()

    init {
        _questionsLiveData.addSource(getQuestionsResult) {
            if (it is Outcome.Success) {
                it.let { resultList ->
                    _questionsLiveData.value = resultList.data
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
//                            _error.value = retrofitException.kind

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
    }

    fun getQuestions() {
        getQuestionsUseCase(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getQuestionsUseCase.onCleared()
    }

}