package org.mhacks.app.postannouncement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.postannouncement.usecase.PostAnnouncementUseCase
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.data.model.PostAnnouncement
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class PostAnnouncementViewModel @Inject constructor(
        private val postAnnouncementUseCase: PostAnnouncementUseCase) : ViewModel() {

    private val postAnnouncementResult = postAnnouncementUseCase.observe()

    private val _createAnnouncement = MediatorLiveData<PostAnnouncement>()

    val createAnnouncement
        get() = _createAnnouncement

    private val _text = MediatorLiveData<Text>()

    val text: LiveData<Text>
        get() = _text

    init {
        _createAnnouncement.addSource(postAnnouncementResult) {
            if (it is Outcome.Success) {
                it.let { createAnnouncement ->
                    _createAnnouncement.value = createAnnouncement.data
                }
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _text.value = Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _text.value = Text.Res(coreR.string.no_internet)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _text.value = Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _text.value = Text.Res(coreR.string.unauthorized_error)
                        }
                    }
                }
            }
        }
    }

    fun postAnnouncement(postAnnouncement: PostAnnouncement) {
        postAnnouncementUseCase.execute(postAnnouncement)
    }

    override fun onCleared() {
        super.onCleared()
        postAnnouncementUseCase.onCleared()
    }

}