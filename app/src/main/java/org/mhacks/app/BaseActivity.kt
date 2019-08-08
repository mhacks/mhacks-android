package org.mhacks.app

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Abstracted class that contains a lot of the UI interactions used throughout the application
 */
abstract class BaseActivity : AppCompatActivity()
//        , NavigationFragment.OnNavigationChangeListener
{

    fun setStatusBarTransparent() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

//    @TargetApi(21)
//    override fun setStatusBarColor(color: Int) {
//        window.statusBarColor = ContextCompat.getColor(this, color)
//    }

    @SuppressLint("InlinedApi")
    fun setSystemFullScreenUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

//    override fun setActionBarColor(@ColorRes colorRes: Int) {
//        val color = ContextCompat.getColor(this, colorRes)
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
//    }
//
//    override fun setFragmentTitle(title: Int) = setTitle(title)
//
//    fun setBottomNavigationColor(color: NavigationColor) {
//        val colorStateList = NavigationColor.getColorStateList(
//                ContextCompat.getColor(this, color.primaryColor),
//                ContextCompat.getColor(this, color.secondaryColor)
//        )
//        main_activity_navigation?.itemIconTintList = colorStateList
//        main_activity_navigation?.itemTextColor = colorStateList
//    }
//
//    // Used for Map View to have a transparent Action Bar.
//    override fun addPadding() {
//        val height = ResourceUtil.convertDpResToPixel(context = this,
//                res = R.dimen.toolbar_height)
//        main_activity_fragment_container?.setPadding(0, height, 0, 0)
//    }
//
//    override fun removePadding() {
//        main_activity_fragment_container?.setPadding(0, 0, 0, 0)
//    }

    fun showToast(stringRes: Int) {
        Toast.makeText(this,
                stringRes,
                Toast.LENGTH_LONG).show()
    }
}