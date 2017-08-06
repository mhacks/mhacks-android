package com.mhacks.android.ui.kotlin.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.mhacks.android.data.model.Floor
import com.mhacks.android.data.network.HackathonCallback
import com.mhacks.android.data.network.NetworkManager
import org.mhacks.android.R
import java.util.ArrayList

/**
 * Created by Shashank on 7/15/2017.
    Displays Maps of the MHacks 10 Venues
 */


class MapViewFragment : Fragment(), OnMapReadyCallback {
    lateinit var nameView: Spinner
    // Data
    lateinit internal var floors: ArrayList<Floor>
    // Views
    private var mMapFragView: View? = null
    // Map
    private var mMapFragment: SupportMapFragment? = null
    private var mGoogleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mMapFragView == null) mMapFragView = inflater!!.inflate(R.layout.fragment_map, container, false)

        nameView = mMapFragView!!.findViewById<Spinner>(R.id.name)

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


        return mMapFragView
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

        val settings = mGoogleMap!!.uiSettings
        settings.isCompassEnabled = true
        settings.isTiltGesturesEnabled = true

        val center = CameraUpdateFactory.newLatLng(LatLng(42.292650, -83.714359))
        val zoom = CameraUpdateFactory.zoomTo(17f)
        val uMichigan = LatLngBounds(
                LatLng(42.264257, -83.755700), LatLng(42.301539, -83.703797))
        //mGoogleMap.setLatLngBoundsForCameraTarget(uMichigan);

        if (ContextCompat.checkSelfPermission(this.activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                //ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        if (ContextCompat.checkSelfPermission(this.activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap!!.isMyLocationEnabled = true
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
        } else {
            //Permission was denied.
        }

        mGoogleMap!!.moveCamera(center)
        mGoogleMap!!.animateCamera(zoom)
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
