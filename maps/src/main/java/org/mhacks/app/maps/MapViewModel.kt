package org.mhacks.app.maps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.maps.data.model.MapFloor
import org.mhacks.app.maps.usecase.GetAndCacheMapResultUseCase
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

data class MapResult(val floorImage: Bitmap, val mapFloor: MapFloor)

class MapViewModel @Inject constructor(
        private val getAndCacheMapResultUseCase: GetAndCacheMapResultUseCase) : ViewModel() {

    private val _getAndCacheMapFloorResult = getAndCacheMapResultUseCase.observe()

    private val _mapResult = MediatorLiveData<MapResult>()

    val mapResult
        get() = _mapResult

    private val _snackBarMessage = MediatorLiveData<Text>()

    val snackBarMessage: LiveData<Text>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _mapResult.addSource(_getAndCacheMapFloorResult) {
            if (it is Outcome.Success) {
                it.let { mapResult ->
                    _mapResult.value = mapResult.data
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
                            _error.value = retrofitException.kind

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unauthorized_error)
                        }
                    }
                }
            }
        }
    }

    fun getAndCacheMapResult() {
        getAndCacheMapResultUseCase.execute(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getAndCacheMapResultUseCase.onCleared()
    }

}