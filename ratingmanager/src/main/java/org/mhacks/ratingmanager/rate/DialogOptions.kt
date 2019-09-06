package org.mhacks.ratingmanager.rate

import android.content.Context
import android.view.View
import org.mhacks.ratingmanager.R
import java.lang.ref.WeakReference

class DialogOptions {

    private var showNeutralButton = true

    private var showNegativeButton = true

    private var showTitle = true

    var cancelable = false

    var storeType = StoreType.GOOGLEPLAY

    var titleResId = R.string.rate_dialog_title

    var messageResId = R.string.rate_dialog_message

    var textPositiveResId = R.string.rate_dialog_ok

    var textNeutralResId = R.string.rate_dialog_cancel

    var textNegativeResId = R.string.rate_dialog_no

    private var titleText: String? = null

    private var messageText: String? = null

    private var positiveText: String? = null

    private var neutralText: String? = null

    private var negativeText: String? = null

    var view: View? = null

    private var _listener: WeakReference<OnClickButtonListener>? = null

    var listener: OnClickButtonListener?
        get() = _listener?.get()
        set(value) { _listener = WeakReference(value!!) }

    fun shouldShowNeutralButton(): Boolean {
        return showNeutralButton
    }

    fun setShowNeutralButton(showNeutralButton: Boolean) {
        this.showNeutralButton = showNeutralButton
    }

    fun shouldShowNegativeButton(): Boolean {
        return showNegativeButton
    }

    fun setShowNegativeButton(showNegativeButton: Boolean) {
        this.showNegativeButton = showNegativeButton
    }

    fun shouldShowTitle(): Boolean {
        return showTitle
    }

    fun setShowTitle(showTitle: Boolean) {
        this.showTitle = showTitle
    }

    fun getTitleText(context: Context): String {
        return if (titleText == null) {
            context.getString(titleResId)
        } else titleText!!
    }

    fun setTitleText(titleText: String) {
        this.titleText = titleText
    }

    fun getMessageText(context: Context): String {
        return if (messageText == null) {
            context.getString(messageResId)
        } else messageText!!
    }

    fun setMessageText(messageText: String) {
        this.messageText = messageText
    }

    fun getPositiveText(context: Context): String {
        return if (positiveText == null) {
            context.getString(textPositiveResId)
        } else positiveText!!
    }

    fun setPositiveText(positiveText: String) {
        this.positiveText = positiveText
    }

    fun getNeutralText(context: Context): String {
        return if (neutralText == null) {
            context.getString(textNeutralResId)
        } else neutralText!!
    }

    fun setNeutralText(neutralText: String) {
        this.neutralText = neutralText
    }

    fun getNegativeText(context: Context): String {
        return if (negativeText == null) {
            context.getString(textNegativeResId)
        } else negativeText!!
    }

    fun setNegativeText(negativeText: String) {
        this.negativeText = negativeText
    }
}
