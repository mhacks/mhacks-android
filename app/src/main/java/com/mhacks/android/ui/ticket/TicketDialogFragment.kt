package com.mhacks.android.ui.ticket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_ticket_dialog.*
import net.glxn.qrgen.android.QRCode
import org.mhacks.android.R

/**
 * Created by jeffreychang on 8/26/17.
 */
class TicketDialogFragment : DialogFragment() {

    lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            key = arguments.getString(ARG_EXTRA_QR_ID)

        } else {
            Log.e(TAG, "The Ticket Dialog Fragment needs the ticket id to work!")
            dismiss()
        }
        super.onCreate(savedInstanceState)
    }


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

        val width = (resources.displayMetrics.widthPixels * .85).toInt()
        val height = (resources.displayMetrics.heightPixels* .7).toInt()

        dialog.getWindow().setLayout(width, height)


        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater!!.inflate(R.layout.fragment_ticket_dialog, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val qr = QRCode.from(key)
                .withSize(500, 500)
                .withColor(0xFF43384D.toInt(), 0x00FFFFFF)
                .bitmap()
        ticket_qr_code_image_view.setImageBitmap(qr)
        ticket_bottom_bar_done_button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                dismiss()
            }

        })

    }

    companion object {

        private val TAG = "TicketDialogFragment"

        private val ARG_EXTRA_QR_ID: String? = "EXTRA_QR_ID"

        fun newInstance(id: String): TicketDialogFragment {
            val fragment = TicketDialogFragment()
            val args = Bundle()
            args.putString(ARG_EXTRA_QR_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}