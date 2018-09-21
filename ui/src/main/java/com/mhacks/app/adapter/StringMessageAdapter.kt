package com.mhacks.app.adapter

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.common.ProgressWheel

@BindingAdapter(value = ["textMessage"], requireAll = false)
fun setTextMessage(textView: TextView, textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        textView.setText(it)
    }
    textMessage?.text?.let {
        textView.text = it
    }
}

@BindingAdapter(value = ["textMessage"], requireAll = false)
fun setTextMessage(progressWheel: ProgressWheel, textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        val text = progressWheel.context.getString(it)
        progressWheel.setCenterText(text)
    }
    textMessage?.text?.let {
        progressWheel.setCenterText(it)
    }
}