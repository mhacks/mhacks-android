package com.mhacks.app.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import org.mhacks.mhacksui.R
import dagger.android.support.DaggerFragment

/**
 * The base which Fragments in this project will extend.
 */
abstract class BaseBindingFragment : DaggerFragment() {

    abstract var rootView: View?

    private val parent by lazy {
        context?.let {
            FrameLayout(it)
        }
    }

    private val progressBarView by lazy {
        context?.let {
            ProgressBarView(it)
        }
    }


    private val errorView by lazy {
        context?.let {
            ErrorableView(it, null)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        parent?.layoutParams = layoutParams
        progressBarView?.layoutParams = layoutParams
        errorView?.layoutParams = layoutParams
        progressBarView?.visibility = View.GONE
        errorView?.visibility = View.GONE

        parent?.addView(rootView)
        parent?.addView(progressBarView)
        parent?.addView(errorView)

        return parent
    }

    fun showProgressBar(loadingText: String) {
        rootView?.visibility = View.GONE
        progressBarView?.loadingText = loadingText
        progressBarView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    fun showMainContent() {
        progressBarView?.visibility = View.GONE
        rootView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    fun showErrorView(error: Int, callback: () -> Unit) {
        progressBarView?.visibility = View.GONE
        rootView?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
        errorView?.titleText = error
        errorView?.iconDrawable = R.drawable.ic_cloud_off_black_24dp
        errorView?.tryAgainCallback = callback
    }
}
