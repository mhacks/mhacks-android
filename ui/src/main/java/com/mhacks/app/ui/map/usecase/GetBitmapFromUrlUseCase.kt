package com.mhacks.app.ui.map.usecase

import com.mhacks.app.data.models.MapFloor
import com.mhacks.app.data.repository.MapFloorsRepository
import com.mhacks.app.di.SingleUseCase
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetBitmapFromUrlUseCase @Inject constructor(
        private val mapFloorsRepository: MapFloorsRepository)
    : SingleUseCase<Unit, List<MapFloor>>() {

    override fun getSingle(parameters: Unit) =
            mapFloorsRepository
                    .getMapFloorLocal()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (it.isEmpty())
                            mapFloorsRepository.getMapRemote()
                                    .doOnSuccess { mapFloors ->
                                        mapFloorsRepository
                                                .deleteAndUpdateMapFloors(mapFloors)
                                    }
                        else Single.just(it)
                    }!!

}
