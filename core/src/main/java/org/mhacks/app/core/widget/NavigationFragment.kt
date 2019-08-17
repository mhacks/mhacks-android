package org.mhacks.app.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import org.mhacks.app.core.R

/**
 * The base which Fragments in this project will extend.
 */
abstract class NavigationFragment : BaseFragment() {

    private var callback: OnNavigationChangeListener? = null

    @get:ColorRes
    abstract var transparentToolbarColor: Int?

    abstract var appBarTitle: Int

    override fun onAttach(context: Context) {
        context.let { super.onAttach(it) }
        callback = activity as? OnNavigationChangeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeColors()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun changeColors() {
        callback?.setFragmentTitle(appBarTitle)
        if (transparentToolbarColor != null) {
            callback?.setActionBarColor(transparentToolbarColor!!)
            callback?.setStatusBarColor(android.R.color.transparent)
            callback?.removePadding()
        } else {
            callback?.setActionBarColor(R.color.colorPrimary)
            callback?.setStatusBarColor(R.color.colorPrimaryDark)
            callback?.addPadding()
        }
    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(@StringRes title: Int)

        fun setActionBarColor(@ColorRes colorRes: Int)

        fun setStatusBarColor(color: Int)

        fun addPadding()

        fun removePadding()
    }

}