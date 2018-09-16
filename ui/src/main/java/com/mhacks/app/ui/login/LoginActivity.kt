package com.mhacks.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import org.mhacks.mhacksui.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.login.signin.view.LoginSignInFragment
import com.mhacks.app.ui.main.MainActivity

class LoginActivity : BaseActivity(), LoginSignInFragment.OnFromLoginFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)
        setStatusBarTransparent()
        setContentView(R.layout.activity_login)
        goToViewPagerFragment(LoginSignInFragment.instance)
    }


    override fun showSnackBar(text: String) {
        Snackbar.make(findViewById(android.R.id.content),
                text, Snackbar.LENGTH_SHORT).show()
    }

    override fun goToMainActivity() =
            startActivity(Intent(this, MainActivity::class.java))

    override fun goToViewPagerFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .commit()
    }
}
