package com.mhacks.app.adapter

import android.databinding.BindingAdapter
import android.widget.TextView
import com.mhacks.app.data.models.common.TextMessage

@BindingAdapter(value = ["textMessage"], requireAll = false)
fun setTextMessage(textView: TextView, textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        textView.setText(it)
    }
    textMessage?.text?.let {
        textView.text = it
    }
}