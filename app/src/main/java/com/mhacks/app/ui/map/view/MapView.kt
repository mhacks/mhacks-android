package com.mhacks.app.ui.map.view

import com.mhacks.app.data.kotlin.Floor

/**
 * View contract for the map fragment.
 */

interface MapView {

    fun onGetMapFloorsSuccess(mapFloors: List<Floor>)

    fun onGetMapFloorsFailure(error: Throwable)
}