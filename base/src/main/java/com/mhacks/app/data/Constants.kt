package com.mhacks.app.data

import android.net.wifi.WifiConfiguration

object Constants {

    const val STAGING_URL = "https://staging.mhacks.org/v1/"

    const val RELEASE_URL = "https://mhacks.org/v1/"

    const val INSTANT_APP_PATH = "/schedule"

    const val FIXED_START_DATE = "2018-05-01T00:00:00" // Using ISO Local Date and Time

    private const val WIFI_SSID = "MHacks11"

    private const val WIFI_PASSWORD = "anyonecancode"

    val WIFI_CONFIGUATION: WifiConfiguration
        get() {
            val wifiConfig = WifiConfiguration()

            wifiConfig.SSID = String.format("\"%s\"", WIFI_SSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", WIFI_PASSWORD)

            return wifiConfig
        }
}