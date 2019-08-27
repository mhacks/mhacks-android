package org.mhacks.app.signin.ui

import android.content.Intent
import android.os.Bundle
import org.mhacks.app.signin.R
import org.mhacks.app.core.widget.BaseActivity
import org.mhacks.app.ui.main.MainActivity
import org.mhacks.app.R as appR

class SignInActivity : BaseActivity(), SignInFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(appR.style.Theme_MHacks_NoActionBar)
        setStatusBarTransparent()
        setContentView(R.layout.activity_signin)
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
