package com.mhacks.app.ui.info

import android.content.ClipData
import android.content.ClipboardManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.mhacks.app.extension.quoteSsidAndPassword
import com.mhacks.app.extension.unquoteSsidAndPassword
import javax.inject.Inject

class WifiInstaller @Inject constructor(
        private val wifiManager: WifiManager,
        private val clipboardManager: ClipboardManager
) {
    fun installConferenceWifi(rawWifiConfig: WifiConfiguration): Boolean {
        val conferenceWifiConfig = rawWifiConfig.quoteSsidAndPassword()
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        var success = false
        val netId = wifiManager.addNetwork(conferenceWifiConfig)
        if (netId != -1) {
            wifiManager.enableNetwork(netId, false)
            success = true
        } else {
            val clip: ClipData =
                    ClipData.newPlainText(
                            "wifi_password",
                            conferenceWifiConfig.unquoteSsidAndPassword().preSharedKey
                    )
            clipboardManager.primaryClip = clip
        }
        return success
    }
}