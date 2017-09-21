package com.mhacks.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.network.services.HackathonApiService
import com.mhacks.android.data.room.MHacksDatabase
import com.mhacks.android.ui.MainActivity
import com.mhacks.android.ui.common.BaseActivity
import com.mhacks.android.ui.login.components.LoginFragment
import com.mhacks.android.util.NetworkUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.android.R
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginFragment.OnFromLoginFragmentCallback{

    @Inject lateinit var mhacksDatabase: MHacksDatabase
    @Inject lateinit var hackathonService: HackathonApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MHacksApplication).hackathonComponent.inject(this)
        setTheme(R.style.MHacksTheme)
        setStatusBarTransparent()
        setContentView(R.layout.activity_login)
        goToViewPagerFragment(LoginFragment.instance)
    }


    override fun showSnackBar(text: String) {
        Snackbar.make(findViewById(android.R.id.content),
                text, Snackbar.LENGTH_SHORT).show()
    }


    override fun skipAndGoToMainActivity() {

        // Stores an empty Login so we know that the user skipped the login.

        Observable.fromCallable({
            mhacksDatabase
                    .loginDao()
                    .insertLogin(
                            Login(0, true, false, "", "")
                    )})
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()


        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun loggedInAndGoToMainActivity(login: Login) {

        login.id = 0
        Observable.fromCallable({
            mhacksDatabase
                    .loginDao()
                    .insertLogin(login)})
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun attemptLogin(email: String, password: String) {
        if (NetworkUtil.checkIfNetworkSucceeds(context = this)) {
            hackathonService.postLogin(email, password)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response -> loggedInAndGoToMainActivity(response)},
                            { error ->
                                when (error.message) {
                                // The space is nescessary.
                                    "HTTP 401 " ->
                                        showSnackBar("The username and password is incorrect.")
                                }
                            }
                    ) }
    }

    override fun goToViewPagerFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .commit()
    }
}
