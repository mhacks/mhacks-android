package org.mhacks.app

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mhacks.app.core.widget.NavigationFragment
import org.mhacks.app.util.ResourceUtil

/**
 * Abstracted class that contains a lot of the UI interactions used throughout the application
 */
abstract class BaseActivity : AppCompatActivity(), NavigationFragment.OnNavigationChangeListener {

    abstract val bottomNavigationView: BottomNavigationView

    abstract val containerView: FrameLayout

    fun setStatusBarTransparent() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    fun setSystemFullScreenUI() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }

    override fun setActionBarColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setFragmentTitle(title: Int) = setTitle(title)

    fun setBottomNavigationColor(color: NavigationColor) {
        bottomNavigationView.itemIconTintList = color.getColorStateList()
        bottomNavigationView.itemTextColor = color.getColorStateList()
    }

    // Used for Map View to have a transparent Action Bar.
    override fun addPadding() {
        val height = ResourceUtil.convertDpResToPixel(context = this,
                res = R.dimen.toolbar_height)
        containerView.setPadding(0, height, 0, 0)
    }

    override fun removePadding() {
        containerView.setPadding(0, 0, 0, 0)
    }
}

data class NavigationColor(val primaryColor: Int, val secondaryColor: Int) {
    fun getColorStateList(): ColorStateList {
        val states = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
        )
        val colors = intArrayOf(primaryColor, secondaryColor)
        return ColorStateList(states, colors)
    }
}