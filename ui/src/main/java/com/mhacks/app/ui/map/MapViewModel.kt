package com.mhacks.app.ui.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.mhacks.app.data.models.MapFloor
import com.mhacks.app.data.models.Result
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

    private val _error: MutableLiveData<Result.Error.Kind> = MutableLiveData()

    val error: LiveData<Result.Error.Kind>
        get() = _error

    init {
        _mapResult.addSource(_getAndCacheMapFloorResult) {
            if (it is Result.Success) {
                it.let { mapResult ->
                    _mapResult.value = mapResult.data
                }
            } else if (it is Result.Error<*>) {
                when (it.kind) {
                    Result.Error.Kind.NETWORK-> {
                        _error.value = it.kind
                    }
                    else -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.unknown_error, null)
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