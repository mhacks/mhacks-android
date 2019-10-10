package org.mhacks.app.ui.main

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.mhacks.app.R
import org.mhacks.app.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
                DataBindingUtil.setContentView<ActivitySplashBinding>(
                        this, R.layout.activity_splash
                )

        (binding.activitySplashAnimationImageView.drawable as? AnimatedVectorDrawable)?.let {
            it.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    startActivity(
                            Intent(this@SplashActivity, MainActivity::class.java)
                    )
                }
            })
            it.start()
        }

    }

}