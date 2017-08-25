package com.mhacks.android.ui.common

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.android.R


/**
 * The base which Fragments in this project will extend.
 */

abstract class BaseFragment : Fragment() {

    private var mCallback: OnNavigationChangeListener? = null

    abstract var setTransparent: Boolean set
    abstract var AppBarTitle: Int set
    abstract var NavigationColor: NavigationColor set
    abstract var LayoutResourceID: Int set
    abstract var configureView: (view: View) -> Unit? set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCallback = activity as OnNavigationChangeListener
    }

    override final   fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changeColors()
        val view = inflater!!.inflate(LayoutResourceID, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureView(view!!)
    }

    /* http://mrtn.me/blog/2012/03/17/get-the-height-of-the-status-bar-in-android/
        Copied code to get the length of the status bar.
     */

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun changeColors() {
        mCallback!!.setFragmentTitle(AppBarTitle)
        mCallback!!.setActionBarColor(android.R.color.transparent)
        mCallback!!.setBottomNavigationColor(NavigationColor)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (setTransparent) {
                mCallback!!.removePadding()
                mCallback!!.setActionBarColor(android.R.color.transparent)
                mCallback!!.setStatusBarColor(android.R.color.transparent)
                mCallback!!.addToolbarPadding()
                mCallback!!.setLayoutFullScreen()

            } else {
                mCallback!!.addPadding()
                mCallback!!.setActionBarColor(R.color.primary)
                mCallback!!.setStatusBarColor(R.color.primary_dark)
//                mCallback!!.removeToolbarPadding()
                mCallback!!.removeLayoutFullScreen()
            }

        } else {

        }
//            mCallback!!.setStatusBarColor(android.R.color.transparent)
//
//
//            } else {
////                mCallback!!.clearTransparentStatusBar()
////                mCallback!!.setStatusBarColor(setTransparent)
//
//            }
//        } else {
//            mCallback!!.setTransparentStatusBar()
//            mCallback!!.addPadding()

//        }
    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(title: Int)

        fun setActionBarColor(color: Int)

        fun setTransparentStatusBar()

        fun clearTransparentStatusBar()

        fun setLayoutFullScreen()

        fun removeLayoutFullScreen()

        fun setStatusBarColor(color: Int)

        fun setBottomNavigationColor(color: NavigationColor)

        fun addPadding()

        fun removePadding()

        fun addToolbarPadding()

        fun removeToolbarPadding()
    }
}