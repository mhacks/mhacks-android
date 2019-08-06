package org.mhacks.app

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.DaggerCoreComponent

private const val MHACKS_GROUP = "MHacks Group"

class MHacksApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    override fun onCreate() {
        super.onCreate()
        val nightMode = if (BuildCompat.isAtLeastQ()) {
            MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            MODE_NIGHT_AUTO_BATTERY
        }
        setDefaultNightMode(nightMode)
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
