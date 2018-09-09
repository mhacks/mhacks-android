package com.mhacks.app.ui.map.view

import com.mhacks.app.data.models.Floor

/**
 * View contract for the map fragment.
 */

interface MapView {

    fun onGetMapFloorsSuccess(mapFloors: List<Floor>)

    fun onGetMapFloorsFailure(error: Throwable)
}