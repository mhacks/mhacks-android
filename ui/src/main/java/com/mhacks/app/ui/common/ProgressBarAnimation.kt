package com.mhacks.app.ui.common

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * @param fullDuration - time required to fill progress from 0% to 100%
 */
class ProgressBarAnimation(
        private val progressBar: ProgressBar,
        fullDuration: Long) : Animation() {
    private var to: Int = 0
    private var from: Int = 0
    private val stepDuration: Long = fullDuration / progressBar.max

    init {
        interpolator = FastOutSlowInInterpolator()
    }

    fun setProgress(_progress: Int) {
        var progress = _progress
        if (progress < 0) {
            progress = 0
        }

        if (progress > progressBar.max) {
            progress = progressBar.max
        }

        to = progress

        from = progressBar.progress
        duration = Math.abs(to - from) * stepDuration
        progressBar.startAnimation(this)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
    }
}