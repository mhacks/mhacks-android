package org.mhacks.ratingmanager.rate

import android.content.Context
import androidx.appcompat.app.AlertDialog
import org.mhacks.ratingmanager.rate.IntentHelper.createIntentForAmazonAppstore
import org.mhacks.ratingmanager.rate.IntentHelper.createIntentForGooglePlay
import org.mhacks.ratingmanager.rate.PreferenceHelper.setAgreeShowDialog
import org.mhacks.ratingmanager.rate.PreferenceHelper.setRemindInterval

object DialogManager {

    private fun getDialogBuilder(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    fun create(context: Context, options: DialogOptions): AlertDialog {
        val builder = getDialogBuilder(context)
        builder.setMessage(options.getMessageText(context))

        if (options.shouldShowTitle()) builder.setTitle(options.getTitleText(context))

        builder.setCancelable(options.cancelable)

        val view = options.view
        if (view != null) builder.setView(view)

        val listener = options.listener

        builder.setPositiveButton(options.getPositiveText(context)) { _, which ->
            val intentToAppstore = if (options.storeType === StoreType.GOOGLEPLAY)
                createIntentForGooglePlay(context)
            else
                createIntentForAmazonAppstore(context)
            context.startActivity(intentToAppstore)
            setAgreeShowDialog(context, false)
            listener?.onClickButton(which)
        }

        if (options.shouldShowNeutralButton()) {
            builder.setNeutralButton(options.getNeutralText(context)) { _, which ->
                setRemindInterval(context)
                listener?.onClickButton(which)
            }
        }

        if (options.shouldShowNegativeButton()) {
            builder.setNegativeButton(options.getNegativeText(context)) { dialog, which ->
                setAgreeShowDialog(context, false)
                listener?.onClickButton(which)
            }
        }

        return builder.create()
    }

}