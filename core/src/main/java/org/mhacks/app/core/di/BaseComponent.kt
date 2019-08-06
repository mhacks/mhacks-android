package org.mhacks.app.core.di

import android.app.Activity
import android.app.Service
import androidx.fragment.app.Fragment

interface BaseComponent<T> {

    fun inject(target: T)
}

/**
 * Base dagger component for use in activities.
 */
interface BaseActivityComponent<T : Activity> : BaseComponent<T>

/**
 * Base dagger component for use in fragments.
 */
interface BaseFragmentComponent<T : Fragment> : BaseComponent<T>

/**
 * Base dagger components for use in services.
 */
interface BaseServiceComponent<T : Service> : BaseComponent<T>
