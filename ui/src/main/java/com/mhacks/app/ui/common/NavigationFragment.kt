package com.mhacks.app.ui.common

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.mhacks.mhacksui.R

/**
 * The base which Fragments in this project will extend.
 */
abstract class NavigationFragment : BaseFragment() {

    private var callback: OnNavigationChangeListener? = null

    abstract var setTransparent: Boolean

    abstract var appBarTitle: Int

    override fun onAttach(context: Context) {
        context?.let { super.onAttach(it) }
        callback = activity as? OnNavigationChangeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callback?.setFragmentTitle(appBarTitle)
        super.onViewCreated(view, savedInstanceState)
        changeColors()
    }

    private fun changeColors() {
        callback?.setActionBarColor(android.R.color.transparent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (setTransparent) {
                callback?.setActionBarColor(android.R.color.transparent)
                callback?.setStatusBarColor(android.R.color.transparent)
                callback?.removePadding()

            } else {
                callback?.setActionBarColor(R.color.colorPrimary)
                callback?.setStatusBarColor(R.color.colorPrimaryDark)
                callback?.addPadding()
            }
        }
    }

    fun setCustomActionBarColor(@ColorRes res: Int) {
        callback?.setActionBarColor(res)
    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(@StringRes title: Int)

        fun setActionBarColor(@ColorRes colorRes: Int)

        fun setStatusBarColor(color: Int)

        fun addPadding()

        fun removePadding()
    }

}