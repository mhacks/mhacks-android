package com.mhacks.android.ui.kotlin.map

import com.mhacks.android.data.model.Location
import com.mhacks.android.data.network.HackathonCallback
import com.mhacks.android.data.network.NetworkManager
import java.util.ArrayList

/**
 * Created by Shashank on 7/15/2017.
 */

class LocationManager private constructor() {
   /* private lateinit var locations: ArrayList<Location>

    init {
        val networkManager = NetworkManager.getInstance()
        networkManager.getLocations(object : HackathonCallback<List<Location>> {
            override fun success(response: List<Location>) {
                locations = ArrayList(response)
            }

            override fun failure(error: Throwable) {
                locations = ArrayList<Location>()
            }
        })
    }

    companion object {
        private lateinit var instance: LocationManager

        fun getInstance(): LocationManager {
            if (instance == null) {
                instance = LocationManager()
            }

            return instance
        }

        fun getLocation(id: String): Location? {
            val manager = LocationManager.getInstance()

            for (l in manager.locations!!) {
                if (l.getId() == id) return l
            }

            return null
        }
    }*/
}
