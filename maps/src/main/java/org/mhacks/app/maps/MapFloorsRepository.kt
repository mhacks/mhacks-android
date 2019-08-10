package org.mhacks.app.maps

import org.mhacks.app.maps.data.service.GetImageFromUrlService
import org.mhacks.app.data.model.MapFloor
import org.mhacks.app.maps.data.db.MapFloorDao
import org.mhacks.app.maps.data.service.MapService
import javax.inject.Inject

class MapFloorsRepository @Inject constructor(
        private val mapService: MapService,
        private val getImageFromUrlService: GetImageFromUrlService,
        private val mapFloorDao: MapFloorDao) {

    fun getMapFloorCache() = mapFloorDao.getFloors()

    fun getMapRemote() =
            mapService.getFloorResponse()
                    .map {
                        it.floors
                    }

    fun deleteAndUpdateMapFloors(mapFloors: List<MapFloor>) =
            mapFloorDao.deleteAndUpdateMapFloors(mapFloors)

    fun getImageFromUrl(url: String) =
            getImageFromUrlService.getImageFromUrl(url)
}
