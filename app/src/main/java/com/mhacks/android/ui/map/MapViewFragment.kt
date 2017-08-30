package com.mhacks.android.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mhacks.android.data.model.Floor
import com.mhacks.android.data.network.HackathonCallback
import com.mhacks.android.data.network.NetworkManager
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import com.mhacks.android.util.ResourceUtil
import kotlinx.android.synthetic.main.fragment_map.*
import org.mhacks.android.R
import java.util.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup


/**j6yyg
 * Created by anksh on 12/31/2014.
 * Updated by omkarmoghe on 10/6/16

 * Displays maps of the MHacks 8 venues.
 */
class MapViewFragment : BaseFragment(), OnMapReadyCallback {

    override var setTransparent: Boolean = true
    override var AppBarTitle: Int = R.string.title_map
    override var LayoutResourceID: Int = R.layout.fragment_map

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setCustomActionBarColor(R.color.semitransparent_theme_primary)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        if (checkGooglePlayServices(activity)) {
            //        nameView = map_view_name
            setUpMapIfNeeded()

            val networkManager = NetworkManager.getInstance()
            networkManager.getFloors(object : HackathonCallback<List<Floor>> {
                override fun success(response: List<Floor>) {
                    floors = ArrayList(response)

                    val spinnerAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (!floors.isEmpty()) {
                        for (floor in floors) {
                            spinnerAdapter.add(floor.getName())
                        }
                        nameView.adapter = spinnerAdapter
                        spinnerAdapter.notifyDataSetChanged()

                        nameView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>,
                                                        view: View,
                                                        i: Int,
                                                        l: Long) {
                                addOverlay(floors[i])
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {

                            }
                        }
                    }
                }

                override fun failure(error: Throwable) {
                    Log.e(TAG, "unable to get floors", error)
                }
            })
        }

    }

    lateinit var nameView: Spinner
    // Data
    lateinit var floors: ArrayList<Floor>
    // Views
    private var mMapFragView: View? = null
    // Map
    private var mMapFragment: SupportMapFragment? = null
    private var mGoogleMap: GoogleMap? = null


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
        googleMap.setPadding(0, ResourceUtil.convertDpToPixel(context, 60), 0, 0)
        val settings = mGoogleMap!!.uiSettings
        settings.isCompassEnabled = true
        settings.isTiltGesturesEnabled = true

        //CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(42.292650, -83.714359));
        //CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        /*LatLngBounds uMichigan = new LatLngBounds(
                new LatLng(42.264257, -83.755700), new LatLng(42.301539, -83.703797));
        mGoogleMap.setLatLngBoundsForCameraTarget(uMichigan);*/

        val center = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                        .target(LatLng(42.292650, -83.714359))
                        .zoom(15f)
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
        } else {
            //Permission was denied.
        }

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

    private fun addOverlay(floor: Floor) {
        setUpMapIfNeeded()

        // Grab bitmap image
        val networkManager = NetworkManager.getInstance()
        networkManager.getImage(floor.getImage(), object : HackathonCallback<Bitmap> {
            override fun success(response: Bitmap) {
                val image = response
                activity.runOnUiThread {
                    val northCampusBounds = LatLngBounds(
                            // I'm a dumbass so we gotta flip the latitudes
                            LatLng(floor.getSeLatitude(), floor.getNwLongitude()), // South west corner
                            LatLng(floor.getNwLatitude(), floor.getSeLongitude())  // North east corner
                    )

                    val northCampusMap = GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromBitmap(image))
                            .positionFromBounds(northCampusBounds)

                    mGoogleMap!!.addGroundOverlay(northCampusMap)
                }
            }

            override fun failure(error: Throwable) {

            }
        })
    }

    fun checkGooglePlayServices(activity: Activity): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                val dialog = googleApiAvailability.getErrorDialog(activity, status, 1)
                dialog.setTitle("Update Google Play Services")
                dialog.show()
            }
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        setUpMapIfNeeded()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {

        val TAG = "MapViewFragment"

        val instance: MapViewFragment
            get() = MapViewFragment()
    }

}
