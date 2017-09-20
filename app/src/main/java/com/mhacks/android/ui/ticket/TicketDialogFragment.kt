package com.mhacks.android.ui.ticket

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.kotlin.User
import kotlinx.android.synthetic.main.fragment_ticket_dialog.*
import net.glxn.qrgen.android.QRCode
import org.mhacks.android.R

/**
 * Created by jeffreychang on 8/26/17.
 */
class TicketDialogFragment : DialogFragment() {

    private lateinit var key: String

    private val callback by lazy { activity as Callback }

    override fun onResume() {

        val width = (resources.displayMetrics.widthPixels * .85).toInt()
        val height = (resources.displayMetrics.heightPixels* .7).toInt()

        dialog.window.setLayout(width, height)
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

        ticket_bottom_bar_done_button.setOnClickListener { dismiss() }
        callback.checkOrFetchUser(
                { user ->
                    val qr = QRCode.from(user.email)
                            .withSize(500, 500)
                            .withColor(0xFF43384D.toInt(), 0x00FFFFFF)
                            .bitmap()
                    ticket_qr_code_image_view.setImageBitmap(qr)
                    ticket_full_name_text_view.text = user.fullName
                    if (user.university.isEmpty()) {
                        ticket_school_text_view.text = getString(R.string.no_school)
                    }
                },

                { callback.showSnackBar("Couldn't connect to the Internet!")} )

        ticket_bottom_bar_done_button.setOnClickListener({ dismiss() })

    }

    interface Callback {

        fun checkOrFetchUser(
                success: (user: User) -> Unit,
                failure: (error: Throwable) -> Unit)

        fun startLoginActivity()

        fun showSnackBar(text: String)

    }

    companion object {

        fun newInstance(): TicketDialogFragment {
            val fragment = TicketDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

    }
}


