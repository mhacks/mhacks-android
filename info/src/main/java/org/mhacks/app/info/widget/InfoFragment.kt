package org.mhacks.app.info.widget

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.mhacks.app.core.widget.NavigationFragment
import org.mhacks.app.info.data.model.Info
import org.mhacks.app.info.R
import org.mhacks.app.R as mainR
import org.mhacks.app.info.databinding.FragmentInfoBinding
import timber.log.Timber
import javax.inject.Inject
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.Q
import androidx.recyclerview.widget.LinearLayoutManager
import org.mhacks.app.BuildConfig
import org.mhacks.app.BuildConfig.WIFI_PASSWORD
import org.mhacks.app.BuildConfig.WIFI_SSID
import org.mhacks.app.info.WifiInstaller
import org.mhacks.app.info.di.inject

class InfoFragment: NavigationFragment() {

    @Inject lateinit var wiFiInstaller: WifiInstaller

    override var transparentToolbarColor: Int? = null

    override var appBarTitle: Int = mainR.string.title_info

    override var rootView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        inject()
        FragmentInfoBinding.inflate(
                inflater, container, false).apply {
            context?.let {
                infoFragmentInfoRecyclerView.layoutManager = LinearLayoutManager(
                        it,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
                infoFragmentInfoRecyclerView.addItemDecoration(
                        SpacingItemDecoration(it, 6)
                )

                val adapter = InfoCardRecyclerViewAdapter()
                adapter.infoCallback = ::onItemSelected
                infoFragmentInfoRecyclerView.adapter = adapter
                rootView = root
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onItemSelected(info: Info) {
        Timber.d("%s Clicked!", info.type.toString())

        context?.let {
            when (info.type) {
                Info.TYPE.WIFI -> {
                    if (Build.VERSION.SDK_INT == Q) {
                        val isWifiInstall = wiFiInstaller.installConferenceWifi(WIFI_NETWORK_SUGGESTION)
                        val wifiMessage = if (isWifiInstall) {
                            R.string.wifi_install_success
                        } else {
                            R.string.wifi_install_failure
                        }
                        Toast.makeText(it, wifiMessage, Toast.LENGTH_SHORT).show()
                        Timber.d("Installed network: %s", isWifiInstall.toString())
                    } else {
                        wiFiInstaller.copyWifiPassword()
                        Toast.makeText(it, R.string.wifi_install_failure, Toast.LENGTH_SHORT).show()
                    }
                }
                Info.TYPE.ADDRESS -> {
                    // Replace the spaces with addition for URI parsing.
                    val formattedAddress =
                            it.getString(R.string.info_address)
                                    .replace(" ", "+" )

                    val googleMapsUrl = GOOGLE_MAPS_URL + formattedAddress

                    Timber.d("Opening $formattedAddress in Google Maps")

                    val i = Intent(ACTION_VIEW, Uri.parse(googleMapsUrl))
                    i.component = ComponentName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity")
                    try {
                        startActivity(i)
                    } catch (e: ActivityNotFoundException){
                        Toast.makeText(
                                it,
                                getString(R.string.google_maps_error),
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                Info.TYPE.SLACK -> {
                    val i = Intent(ACTION_VIEW, Uri.parse(
                            BuildConfig.SLACK_INVITE_URL)
                    )
                    i.action = ACTION_VIEW
                    startActivity(i)
                }
                Info.TYPE.EMAIL -> {
                    val i = Intent(
                            Intent.ACTION_SENDTO,
                            Uri.fromParts(
                                    "mailto",
                                    BuildConfig.MHACKS_EMAIL,
                                    null))
                    i.putExtra(Intent.EXTRA_TEXT, "Sent from Android app.")
                    startActivity(Intent.createChooser(i, "Send Email"))
                }
            }
        }
    }

    companion object {

        const val GOOGLE_MAPS_URL = "http://maps.google.co.in/maps?q="

        val WIFI_NETWORK_SUGGESTION: WifiNetworkSuggestion
            @TargetApi(Q)
            get() {
                return WifiNetworkSuggestion.Builder()
                        .setSsid(WIFI_SSID)
                        .setWpa2Passphrase(WIFI_PASSWORD)
                        .build()
            }
    }
}