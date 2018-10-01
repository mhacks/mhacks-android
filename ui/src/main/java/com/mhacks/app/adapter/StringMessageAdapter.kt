package com.mhacks.app.adapter

import androidx.databinding.BindingAdapter
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.common.ProgressWheel

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