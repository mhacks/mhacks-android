package com.mhacks.app.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.mhacks.mhacksui.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.main.MainActivity

class SignInActivity : BaseActivity(),
        SignInFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)
        setStatusBarTransparent()
        setContentView(R.layout.activity_login)
        startViewPagerFragment(SignInFragment.instance)
    }

    override fun startMainActivity() =
            startActivity(Intent(this, MainActivity::class.java))

    override fun startViewPagerFragment(fragment: Fragment) {
//        supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.login_container, fragment)
//                .commit()
    }
}
