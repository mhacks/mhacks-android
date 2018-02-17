package com.mhacks.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.mhacks.app.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.login.signin.view.LoginSignInFragment
import com.mhacks.app.ui.main.view.MainActivity

class LoginActivity : BaseActivity(), LoginSignInFragment.OnFromLoginFragmentCallback{

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


    override fun skipAndGoToMainActivity() {

        // Stores an empty LoginResponse so we know that the user skipped the login.

//        Observable.fromCallable({
//            mhacksDatabase
//                    .loginDao()
//                    .insertLogin(
//                            LoginResponse(0, true, false, "", "")
//                    )})
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//
//
//        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun loggedInAndGoToMainActivity() =
            startActivity(Intent(this, MainActivity::class.java))

    override fun attemptLogin(email: String, password: String) {

    }

    override fun goToViewPagerFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .commit()
    }
}
