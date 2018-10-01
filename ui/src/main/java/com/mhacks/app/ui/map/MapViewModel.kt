package com.mhacks.app.ui.map

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhacks.app.data.models.MapFloor
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.map.usecase.GetAndCacheMapResultUseCase
import org.mhacks.mhacksui.R
import javax.inject.Inject

class MapViewModel @Inject constructor(
        private val getAndCacheMapResultUseCase: GetAndCacheMapResultUseCase): ViewModel() {

    private val _getAndCacheMapFloorResult = getAndCacheMapResultUseCase.observe()

    private val _mapResult = MediatorLiveData<MapResult>()

    val mapResult
        get() = _mapResult

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackBarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _mapResult.addSource(_getAndCacheMapFloorResult) {
            if (it is Result.Success) {
                it.let { mapResult ->
                    _mapResult.value = mapResult.data
                }
            } else if (it is Result.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        TextMessage(
                                                null,
                                                errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _error.value = retrofitException.kind

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unknown_error,
                                            null)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unauthorized_error,
                                            null)
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

    data class MapResult(val floorImage: Bitmap, val mapFloor: MapFloor)

}