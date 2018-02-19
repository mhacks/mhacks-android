package com.mhacks.app.ui.common

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.mhacks.app.R
import com.mhacks.app.ui.common.util.NetworkUtil
import com.mhacks.app.ui.common.util.ResourceUtil
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by jeffreychang on 9/13/17.
 */
abstract class BaseActivity: DaggerAppCompatActivity(),  NavigationFragment.OnNavigationChangeListener  {
    fun setStatusBarTransparent() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    open fun showSnackBar(text: String) {
        Snackbar.make(findViewById(android.R.id.content),
                text,
                Snackbar.LENGTH_SHORT).show()
    }

    open fun checkIfNetworkIsPresent(context: Context, callback: () -> Any) {
        if (!NetworkUtil.checkIfNetworkSucceeds(context)) {
            val snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "No network connection.", Snackbar.LENGTH_LONG)
            snackbar.setAction("Try again", { checkIfNetworkIsPresent(context, callback) })
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        } else callback()
    }
    @TargetApi(21)
    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    @SuppressLint("InlinedApi")
    fun setSystemFullScreenUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @SuppressLint("InlinedApi")
    fun setTransparentStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(21)
    fun clearTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setActionBarColor(@ColorRes color: Int) {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, color))
    }

    override fun setFragmentTitle(title: Int) {
        setTitle(title)
    }

    fun setBottomNavigationColor(color: NavigationColor) {
        val colorStateList = NavigationColor.getColorStateList(
                ContextCompat.getColor(this, color.primaryColor),
                ContextCompat.getColor(this, color.secondaryColor)
        )
        main_activity_navigation?.itemIconTintList = colorStateList
        main_activity_navigation?.itemTextColor = colorStateList
    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param startfragment fragment to replace the main container with
     */

    override fun addPadding() {
        val height: Int = ResourceUtil.convertDpResToPixel(context = this,
                res = R.dimen.toolbar_height)
        fragment_container.setPadding(0, height, 0, 0)
    }

    override fun removePadding() {
        fragment_container.setPadding(0, 0, 0, 0)
    }
}