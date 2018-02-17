package com.mhacks.app.ui.map.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.AdapterView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Floor
import com.mhacks.app.ui.common.BaseFragment
import com.mhacks.app.ui.common.util.GooglePlayUtil
import com.mhacks.app.ui.common.util.NetworkUtil
import com.mhacks.app.ui.common.util.ResourceUtil
import timber.log.Timber
import kotlin.collections.ArrayList


/**
 * Created by anksh on 12/31/2014.
 * Updated by omkarmoghe on 10/6/16

 * Displays maps of the MHacksApplication 8 venues.
 */
class MapViewFragment :
        BaseFragment(), MapView,
        OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    override var setTransparent: Boolean = true
    override var appBarTitle: Int = R.string.title_map
    override var layoutResourceID: Int = R.layout.fragment_map
    lateinit var floors: ArrayList<Floor>
    private var mMapFragment: SupportMapFragment? = null
    private var mGoogleMap: GoogleMap? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        setCustomActionBarColor(R.color.semiColorPrimary)
        if (GooglePlayUtil.checkPlayServices(activity)) setUpMapIfNeeded()
    }

    private fun showDefaultLayoutView() {
        if (this.floors.size != 0) {
            val floor: Floor = floors[0]
            setUpMapIfNeeded()

            NetworkUtil.getImage(floor.floorImage,
                    {success -> onBitmapResponseSuccess(success, floor)},
                    {failure -> onBitmapResponseFailure(failure)})
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("No floor was selected.")
    }

    override fun onItemSelected(parent: AdapterView<*>?, root: View?, position: Int, id: Long) {
        val floor: Floor = floors[position]
        setUpMapIfNeeded()

        NetworkUtil.getImage(floor.floorImage,
                {success -> onBitmapResponseSuccess(success, floor)},
                {failure -> onBitmapResponseFailure(failure)})
    }

    private fun setUpMapIfNeeded() {
        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance()
            fragmentManager.beginTransaction().replace(R.id.map, mMapFragment).commit()
        }
        if (mGoogleMap == null) mMapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.isBuildingsEnabled = true
        googleMap.setPadding(0, ResourceUtil.convertDpToPixel(context,
                100), 0, 0)
        val settings = mGoogleMap!!.uiSettings
        settings.isCompassEnabled = true
        settings.isTiltGesturesEnabled = true
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID

        val center = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                        .target(LatLng(42.292150, -83.715836))
                        .zoom(16.5f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()
        )

        if (ContextCompat.checkSelfPermission(this.activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        if (ContextCompat.checkSelfPermission(this.activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap!!.isMyLocationEnabled = true
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
        } else { }

        googleMap.animateCamera(center)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (permissions.size == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this.activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap!!.isMyLocationEnabled = true
                    mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    private fun onBitmapResponseSuccess(image: Bitmap, floor: Floor) {
        activity.runOnUiThread {
            val northCampusBounds = LatLngBounds (
                LatLng(floor.seLatitude.toDouble(), floor.nwLongitude.toDouble()), //South West corner
                LatLng(floor.nwLatitude.toDouble(), floor.seLongitude.toDouble()) //North East Corner
            )
            val northCampusMap = GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(image))
                    .positionFromBounds(northCampusBounds)
            mGoogleMap!!.addGroundOverlay(northCampusMap)

        }

    }

    @Suppress("UNUSED_PARAMETER")
    private fun onBitmapResponseFailure(error: Throwable) {}

    companion object {
        val instance: MapViewFragment
            get() = MapViewFragment()
    }
}
