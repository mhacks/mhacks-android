package com.mhacks.android.ui.login

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.network.NetworkSingleton
import com.mhacks.android.data.room.RoomSingleton
import com.mhacks.android.ui.login.components.LoginFragment
import org.mhacks.android.R

class LoginActivity : AppCompatActivity() {

    val networkSingleton by lazy {
        NetworkSingleton.newInstance(application = application as MHacksApplication)
    }

    val roomSingleton by lazy {
        RoomSingleton.newInstance(application = application as MHacksApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)
        setStatusBarTransparent()
        setContentView(R.layout.activity_login)
        goToFragment(LoginFragment.instance)
    }

    fun goToFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .commit()
    }

    private fun setStatusBarTransparent() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    companion object {
        val TAG = "LoginActivity"
    }
}
