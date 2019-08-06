package org.mhacks.app.core.widget

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import org.mhacks.app.core.R
import org.mhacks.app.core.databinding.ViewProgressBinding

/**
 * View that displays a progress bar and a message that is the rationale for the progress bar.
 */
class ProgressBarView(context: Context) : ConstraintLayout(context) {

    private var binding: ViewProgressBinding =
            ViewProgressBinding.inflate(LayoutInflater.from(context), this, true)

    var loadingText: String? = null
        set(text) {
            field = text
            binding.progressViewTextViewLoading.text = text

        }

    var isLightMode: Boolean = false
        set(value) {
            binding.apply {
                if (value) setLightMode()
                else setDarkMode()
            }
            field = value
        }

    init {
        isLightMode = false
    }

    private fun setLightMode() {
        val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        setBackgroundColor(Color.WHITE)
        binding.apply {
            progressViewProgressBar.progressDrawable
                    .setColorFilter(colorPrimary, android.graphics.PorterDuff.Mode.SRC_IN)
            progressViewProgressBar.indeterminateDrawable
                    .setColorFilter(colorPrimary, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    private fun setDarkMode() {
        val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        setBackgroundColor(colorPrimary)
        binding.apply {
            progressViewProgressBar.progressDrawable
                    .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
            progressViewProgressBar.indeterminateDrawable
                    .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}