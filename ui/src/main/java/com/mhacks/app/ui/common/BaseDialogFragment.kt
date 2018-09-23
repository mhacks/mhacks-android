package com.mhacks.app.ui.common

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import dagger.android.support.DaggerAppCompatDialogFragment
import org.mhacks.mhacksui.R

/**
 * A dialog fragment that supports dependency injection and data binding
 */
abstract class BaseDialogFragment : DaggerAppCompatDialogFragment() {

    private val parent by lazy {
        activity?.let {
            FrameLayout(it)
        }
    }

    abstract var rootView: View?

    private val progressBarView by lazy {
        activity?.let {
            val progressBarView = ProgressBarView(it)
            progressBarView.isLightMode = true
            progressBarView

        }
    }

    private val errorView by lazy {
        activity?.let {
            ErrorableView(it, null)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TitleDialog)
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

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * .85).toInt()
        val height = (resources.displayMetrics.heightPixels * .7).toInt()
        dialog?.window?.setLayout(width, height)
    }

    fun showProgressBar(loadingText: String) {
        rootView?.visibility = View.GONE
        progressBarView?.loadingText = loadingText
        progressBarView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    fun showProgressBar(@StringRes resId: Int) {
        progressBarView?.visibility = View.VISIBLE
        rootView?.visibility = View.GONE
        context?.let {
            progressBarView?.loadingText = it.getString(resId)
        }

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

