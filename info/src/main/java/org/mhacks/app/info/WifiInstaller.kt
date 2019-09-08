package org.mhacks.app.info

import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build.VERSION_CODES.Q
import org.mhacks.app.BuildConfig.WIFI_PASSWORD

import javax.inject.Inject

class WifiInstaller @Inject constructor(
        private val wifiManager: WifiManager,
        private val clipboardManager: ClipboardManager
) {

    @TargetApi(Q)
    fun installConferenceWifi(wifiSuggestion: WifiNetworkSuggestion) : Boolean {
//        if (!wifiManager.isWifiEnabled) {
//            return false
//        }

        var success = false
        val netId = wifiManager.addNetworkSuggestions(arrayListOf(wifiSuggestion))

        if (netId == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            success = true
        } else {
            copyWifiPassword()
        }
        return success
    }

    fun copyWifiPassword() {
        val clip: ClipData =
                ClipData.newPlainText(
                        "wifi_password",
                        WIFI_PASSWORD
                )
        clipboardManager.setPrimaryClip(clip)
    }
}