package com.mhacks.app.ui.map.usecase

import com.mhacks.app.data.repository.MapFloorsRepository
import com.mhacks.app.di.SingleUseCase
import com.mhacks.app.ui.map.MapViewModel
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheMapResultUseCase @Inject constructor(
        private val mapFloorsRepository: MapFloorsRepository)
    : SingleUseCase<Unit, MapViewModel.MapResult>() {

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
                    }
                    .flatMap { mapFloors ->
                        mapFloorsRepository
                                .getImageFromUrl(mapFloors[0].floorImage)
                                .map {
                                    MapViewModel.MapResult(it, mapFloors[0])
                                }
                    }!!

}
