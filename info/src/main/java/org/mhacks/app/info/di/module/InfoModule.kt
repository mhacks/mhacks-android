package org.mhacks.app.info.di.module

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import dagger.Module
import dagger.Provides
import org.mhacks.app.info.WifiInstaller

/**
 * Provides dependencies for info module
 */
@Module
class InfoModule {

    @Provides
    fun bindWifiManager(context: Context) = context
            .applicationContext
            .getSystemService(WIFI_SERVICE) as WifiManager

    @Provides
    fun bindClipBoardManager(context: Context) = context
            .applicationContext
            .getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    fun bindWifiInstaller(wifiManager: WifiManager, clipBoardManager: ClipboardManager)
            = WifiInstaller(wifiManager, clipBoardManager)

}
