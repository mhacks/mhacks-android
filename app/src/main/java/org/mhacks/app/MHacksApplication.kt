package org.mhacks.app

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import org.mhacks.app.core.DarkModeType
import org.mhacks.app.core.ThemePrefProvider
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.DaggerCoreComponent
import org.mhacks.app.util.FlipperHelper
import org.mhacks.ratingmanager.rate.RatingManager
import javax.inject.Inject

class MHacksApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
                .application(this)
                .build()
    }

    @Inject
    lateinit var networkFlipperPlugin: NetworkFlipperPlugin

    @Inject
    lateinit var themePrefProvider: ThemePrefProvider

    override fun onCreate() {
        super.onCreate()
        inject()
        RatingManager.with(this)
//        Crashlytics.getInstance().crash()
        initFlipper()
        setDarkMode(themePrefProvider.darkModeType)
    }

    private fun initFlipper() {
        FlipperHelper.init(this)
    }

    private fun inject() {
        DaggerApplicationComponent
                .builder()
                .coreComponent(coreComponent)
                .build()
                .inject(this)
    }

    companion object {

        @JvmStatic
        fun coreComponent(context: Context) =
                (context.applicationContext as MHacksApplication).coreComponent
    }
}

fun Activity.coreComponent() = MHacksApplication.coreComponent(this)

// Will throw exception if context is not initialized.
fun Fragment.coreComponent() = MHacksApplication.coreComponent(requireContext().applicationContext)

// Will throw exception if context is not initialized.
fun Service.coreComponent() = MHacksApplication.coreComponent(application.applicationContext)

private fun setDarkMode(darkModeType: DarkModeType) {
    val nightMode =
            when (darkModeType) {
                DarkModeType.DARK -> MODE_NIGHT_YES
                DarkModeType.LIGHT -> MODE_NIGHT_NO
                DarkModeType.SYSTEM_AUTO -> {
                    if (BuildCompat.isAtLeastQ()) MODE_NIGHT_FOLLOW_SYSTEM
                    else MODE_NIGHT_AUTO_BATTERY
                }
            }
    setDefaultNightMode(nightMode)
}

fun Application.setDarkMode(darkModeType: DarkModeType, activity: Activity, targetActivity: Class<*>) {
    setDarkMode(darkModeType)
    startActivity(Intent(activity, targetActivity).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
    })
    activity.finish()
}