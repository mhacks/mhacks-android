package com.mhacks.android.ui.ticket

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.mhacks.android.ui.kotlin.schedule.EventFragment
import org.mhacks.android.R

/**
 * Created by jeffreychang on 8/26/17.
 */
class TicketDialogFragment(): DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(activity)
//            .setMessage(R.string.ticket)
//            .setNegativeButton(R.string.done_positive,
//                object: DialogInterface.OnClickListener {
//                    override fun onClick(view: DialogInterface?, id: Int) {
//                    }
//            })
//            .create()
//    }


    override fun onResume() {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        dialog.getWindow().setLayout(width, height)


        super.onResume()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog

    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
        }
        return inflater!!.inflate(R.layout.fragment_ticket_dialog, container)
    }


    companion object {
        val TAG = "TicketDialogFragment"

        val instance: TicketDialogFragment
            get() = TicketDialogFragment()
    }
}