package com.mhacks.app.ui.map.view

import com.mhacks.app.data.models.MapFloor

/**
 * View contract for the map fragment.
 */

interface MapView {

    fun onGetMapFloorsSuccess(mapFloors: List<MapFloor>)

    fun onGetMapFloorsFailure(error: Throwable)
}