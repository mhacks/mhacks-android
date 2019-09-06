package org.mhacks.ratingmanager.rate

import android.content.Context
import android.net.Uri

private val GOOGLE_PLAY = "https://play.google.com/store/apps/details?id="

private val AMAZON_APPSTORE = "amzn://apps/android?p="

fun getGooglePlay(packageName: String?): Uri? {
    return if (packageName == null) null else Uri.parse(GOOGLE_PLAY + packageName)
}

fun getAmazonAppstore(packageName: String?): Uri? {
    return if (packageName == null) null else Uri.parse(AMAZON_APPSTORE + packageName)
}

fun isPackageExists(context: Context, targetPackage: String): Boolean {
    val pm = context.packageManager
    val packages = pm.getInstalledApplications(0)
    for (packageInfo in packages) {
        if (packageInfo.packageName == targetPackage) return true
    }
    return false
}
