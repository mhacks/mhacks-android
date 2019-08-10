package org.mhacks.app.core.widget

import android.content.Context
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import org.mhacks.app.core.R
import org.mhacks.app.core.databinding.ViewErrorBinding

/**
 * Used to display a screen when there is a point of failure
 */
/**
 * View that displays a screen when the user has lost connectivity.
 *
 * @property[tryAgainCallback] A callback to load to reattempt the network call.
 */
class ErrorView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var binding: ViewErrorBinding =
            ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)

    var iconDrawable: Int = R.drawable.ic_cloud_off_black_24dp
        set(icon) = binding.errorViewImageViewIcon.setImageResource(icon)

    var titleText: Int = R.string.device_offline
        set(title) = binding.errorViewErrorTextView.setText(title)

    @ColorInt
    var textColor: Int? = null
        set(colorRes) {
            binding.apply {
                colorRes?.let {
                    val color = ContextCompat.getColor(context, colorRes)
                    errorViewErrorTextView.setTextColor(color)
                    errorViewErrorDescriptionTextView
                            .setTextColor(color)
                    errorViewImageViewIcon.setColorFilter(color,
                            PorterDuff.Mode.SRC_IN)
                }
            }
        }

    var tryAgainCallback: (() -> Unit)? = null
        set(callback) {
            field = callback
            binding.errorViewErrorDescriptionTextView.setOnClickListener {
                callback?.invoke()
            }
        }

    init {
        val tryAgainSpan = SpannableString(binding.errorViewErrorDescriptionTextView.text)
        tryAgainSpan.setSpan(UnderlineSpan(), 0 , tryAgainSpan.length, 0)
        binding.errorViewErrorDescriptionTextView.text = tryAgainSpan
    }

}