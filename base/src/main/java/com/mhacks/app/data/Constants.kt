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

    const val GOOGLE_MAPS_URL = "http://maps.google.co.in/maps?q="

    const val SLACK_INVITE_URL = "https://join.slack.com/t/mhacks11/shared_invite/enQtNDQzMDI0MDY3ODQ1LTM3YTIyMmJmNjU3NGM2NDk1MzQzZjZmMmY3ZDliZmZlNTRjOGNiMTc2OWRiZjIyODBiNzk2ZGQ4YjBmMjlkZjU"

    const val MHACKS_EMAIL = "hackathon@umich.edu"
}