package com.mhacks.android.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.network.services.HackathonApiService
import com.mhacks.android.data.room.MHacksDatabase
import com.mhacks.android.ui.MainActivity
import com.mhacks.android.ui.login.components.LoginFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.android.R
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginFragment.OnFromLoginFragmentCallback{

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


    override fun goToMainActivity() {
        Observable.fromCallable({
                mhacksDatabase
                        .loginDao()
                        .insertLogin(
                                Login(1, true, false, "", "")
                        )})
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()


        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun attemptLogin(email: String, password: String) {
        hackathonService.postLogin(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> Timber.d(response.token)},
                        { error -> Timber.d(error) }
                )
    }

    override fun goToViewPagerFragment(fragment: android.support.v4.app.Fragment) {
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
