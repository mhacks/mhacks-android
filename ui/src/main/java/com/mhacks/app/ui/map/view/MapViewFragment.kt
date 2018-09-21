package com.mhacks.app.ui.map.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.common.NavigationBindingFragment
import com.mhacks.app.ui.map.MapViewModel
import com.mhacks.app.util.GooglePlayUtil
import com.mhacks.app.util.ResourceUtil
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentMapBinding
import javax.inject.Inject

/**
 * Displays the rooms and terrain of the venue.
 */
class MapViewFragment :
        NavigationBindingFragment(),
        OnMapReadyCallback {

    override var setTransparent: Boolean = true

    override var appBarTitle: Int = R.string.title_map

    override var rootView: View? = null

    private var mapFragment: SupportMapFragment? = null

    private var googleMap: GoogleMap? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        FragmentMapBinding.inflate(inflater, container, false)
                .apply {
                    val viewModel = viewModelProvider<MapViewModel>(viewModelFactory)

                    subscribeUi(viewModel)
                    activity?.let {
                        if (GooglePlayUtil.checkPlayServices(it)) {
                            setUpMapIfNeeded()
                            viewModel.getAndCacheMapResult()
                        }
                    }

                    setLifecycleOwner(this@MapViewFragment)
                    rootView = root
                }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomActionBarColor(R.color.semiColorPrimary)
    }

    private fun subscribeUi(mapFloorViewModel: MapViewModel) {
        mapFloorViewModel.mapResult.observe(this, Observer {
            setUpMapIfNeeded()

            it?.let { mapResult ->
                setupMap(mapResult)
            }

            showMainContent()
        })

        mapFloorViewModel.error.observe(this, Observer { error ->
            when (error) {
                RetrofitException.Kind.NETWORK -> {
                    showErrorView(R.string.maps_network_failure) {
                        showProgressBar(getString(R.string.loading_maps))
                    }
                }
                else -> {
                    // no-op
                }
            }
        })
        mapFloorViewModel.snackBarMessage.observe(this, Observer {
            it?.let { textMessage ->
                rootView?.showSnackBar(
                        Snackbar.LENGTH_SHORT, textMessage)
            }
        })
    }

    private fun setUpMapIfNeeded() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment?.let {
                fragmentManager?.beginTransaction()?.replace(R.id.map_fragment_host_container, it)?.commit()
            }
        }
        if (googleMap == null) mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val padding = context?.let { ResourceUtil.convertDpToPixel(it, 100) } ?: 150
        googleMap.isBuildingsEnabled = true
        googleMap.setPadding(0, padding, 0, 0)
        val settings = this.googleMap?.uiSettings
        settings?.isCompassEnabled = true
        settings?.isTiltGesturesEnabled = true
        this.googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID

        val center = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                        .target(LatLng(42.292150, -83.715836))
                        .zoom(16.5f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()
        )

        if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        if (ContextCompat.checkSelfPermission(
                        activity!!.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap?.isMyLocationEnabled = true
            this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        } else { }

        googleMap.animateCamera(center)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == 1) {
            if (permissions.size == 1 && permissions[0] ==
                    Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
                                Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    googleMap?.isMyLocationEnabled = true
                    googleMap?.uiSettings?.isMyLocationButtonEnabled = true
                }
            } else {
                view?.let {
                    Snackbar.make(it, getString(R.string.overlay_failure), Snackbar.LENGTH_SHORT)
                }
            }
        }
    }

    private fun setupMap(mapResult: MapViewModel.MapResult) {
        val (floorImage, mapFloor) = mapResult

        val northCampusBounds = LatLngBounds(
            LatLng(
                    mapFloor.seLatitude.toDouble(),
                    mapFloor.nwLongitude.toDouble()), // South West corner
            LatLng(
                    mapFloor.nwLatitude.toDouble(),
                    mapFloor.seLongitude.toDouble()) // North East Corner
        )
        val northCampusMap = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(floorImage))
                .positionFromBounds(northCampusBounds)
        googleMap?.addGroundOverlay(northCampusMap)
    }

    companion object {
        val instance: MapViewFragment
            get() = MapViewFragment()
    }
}
