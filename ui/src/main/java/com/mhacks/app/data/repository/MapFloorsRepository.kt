package com.mhacks.app.data.repository

import com.mhacks.app.data.service.GetImageFromUrlService
import com.mhacks.app.data.models.MapFloor
import com.mhacks.app.data.room.dao.MapFloorDao
import com.mhacks.app.data.service.MapService
import javax.inject.Inject

//class MapFloorsRepository @Inject constructor(
//        private val mapService: MapService,
//        private val getImageFromUrlService: GetImageFromUrlService,
//        private val mapFloorDao: MapFloorDao) {
//
//    fun getMapFloorLocal() = mapFloorDao.getFloors()
//
//    fun getMapRemote() =
//            mapService.getFloorResponse()
//                    .map {
//                        it.floors
//                    }!!
//
//    fun deleteAndUpdateMapFloors(mapFloors: List<MapFloor>) =
//            mapFloorDao.deleteAndUpdateMapFloors(mapFloors)
//
//    fun getImageFromUrl(url: String) =
//            getImageFromUrlService.getImageFromUrl(url)
//
//}