package org.mhacks.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import org.mhacks.app.core.di.AppModule
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.DaggerCoreComponent
import org.mhacks.app.ui.main.MainActivity

private const val MHACKS_GROUP = "MHacks Group"

class MHacksApplication : Application() {

    private var debugDarkMode = false

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        val nightMode =
                if (BuildCompat.isAtLeastQ()) MODE_NIGHT_FOLLOW_SYSTEM
                else MODE_NIGHT_AUTO_BATTERY
        setDefaultNightMode(nightMode)
    }

    // For debugging.
    fun toggleDarkMode(activity: Activity) {
        debugDarkMode = !debugDarkMode
        val nightMode =
                if (debugDarkMode) MODE_NIGHT_YES
                else MODE_NIGHT_NO
        setDefaultNightMode(nightMode)
        startActivity(Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
        activity.finish()
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
