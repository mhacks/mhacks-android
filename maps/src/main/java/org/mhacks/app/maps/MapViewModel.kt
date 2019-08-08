package org.mhacks.app.maps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.data.model.MapFloor
import org.mhacks.app.data.model.common.RetrofitException
import timber.log.Timber
import javax.inject.Inject

class MapViewModel @Inject constructor(): ViewModel() {

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
//        _mapResult.addSource(_getAndCacheMapFloorResult) {
//            if (it is Result.Success<*>) {
//                it.let { mapResult ->
//                    _mapResult.value = mapResult.data
//                }
//            } else if (it is Result.Error<*>) {
//                (it.exception as? RetrofitException)?.let { retrofitException ->
//                    when (retrofitException.kind) {
//                        RetrofitException.Kind.HTTP -> {
//                            retrofitException.errorResponse?.let { errorResponse ->
//                                _snackBarMessage.value =
//                                        String(
//                                                null,
//                                                errorResponse.message)
//                            }
//                        }
//                        RetrofitException.Kind.NETWORK -> {
//                            _error.value = retrofitException.kind
//
//                        }
//                        RetrofitException.Kind.UNEXPECTED -> {
//                            _snackBarMessage.value =
//                                    String(
//                                            R.string.unknown_error,
//                                            null)
//                        }
//                        RetrofitException.Kind.UNAUTHORIZED -> {
//                            _snackBarMessage.value =
//                                    String(
//                                            R.string.unauthorized_error,
//                                            null)
//                        }
//                    }
//                }
//            }
//        }
    }

    fun getAndCacheMapResult() {
        // getAndCacheMapResultUseCase.execute(Unit)
        Timber.d("getAndCacheMapResult not implemented")
    }

    override fun onCleared() {
        super.onCleared()
        //getAndCacheMapResultUseCase.onCleared()
    }

    data class MapResult(val floorImage: Bitmap, val mapFloor: MapFloor)

}