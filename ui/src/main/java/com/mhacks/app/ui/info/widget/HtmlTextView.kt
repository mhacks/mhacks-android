package com.mhacks.app.ui.info.widget

import android.content.Context
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class HtmlTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    /**
     * LinkMovementMethod conflicts with text ellipsizing so instead we we listen for touch events
     * and implement link detection (logic borrowed from LinkMovementMethod).
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) && text is Spanned) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= totalPaddingLeft
            y -= totalPaddingTop

            x += scrollX
            y += scrollY

            val layout = layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            val link = (text as Spanned).getSpans(off, off, ClickableSpan::class.java)
            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(this)
                }
                return true
            }
        }
        return false
    }
}