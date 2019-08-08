package org.mhacks.app.maps

import org.mhacks.app.maps.data.service.MapService
import javax.inject.Inject

class MapFloorsRepository @Inject constructor(
        private val mapService: MapService) {
    fun getMap() =
            mapService.getFloorResponse()
                    .map {
                        it.floors
                    }
}
