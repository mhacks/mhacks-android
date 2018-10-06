package com.mhacks.app.ui.ticket

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.mhacks.mhacksui.R
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.ui.common.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_ticket_dialog.*
import net.glxn.qrgen.android.QRCode
import org.mhacks.mhacksui.databinding.FragmentTicketDialogBinding
import javax.inject.Inject

/**
 * Fragment to display user information and show their QR code
 */
class TicketDialogFragment : BaseDialogFragment() {

    @Inject lateinit var ticketViewModel: TicketViewModel

    private var callback: Callback? = null

    override var rootView: View? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as? Callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        FragmentTicketDialogBinding.inflate(
                inflater, container, false).apply {
            ticketBottomBarDoneButton.setOnClickListener {
                dismiss()
            }
            subscribeUi(ticketViewModel, this)

            ticketViewModel.getAndCacheUser()
            setLifecycleOwner(this@TicketDialogFragment)

            rootView = root
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(R.string.loading_ticket)
    }
    private fun subscribeUi(
            ticketViewModel: TicketViewModel,
            binding: FragmentTicketDialogBinding) {
        ticketViewModel.user.observe(this, Observer {
            it?.let { user ->
                val qr = QRCode.from(user.email)
                        .withSize(500, 500)
                        .withColor(0xFF43384D.toInt(), 0x00FFFFFF)
                        .bitmap()
                ticket_qr_code_image_view.setImageBitmap(qr)
                ticket_full_name_text_view.text = user.fullName
                if (user.university.isNullOrEmpty()) {
                    ticket_school_text_view.text = getString(R.string.no_school)
                } else {
                    ticket_school_text_view.text = user.university
                }
                showMainContent()
            }
        })

        ticketViewModel.error.observe(this, Observer { error ->
            when (error) {
                RetrofitException.Kind.NETWORK -> {
                    showErrorView(R.string.ticket_network_error) {
                        ticketViewModel.getAndCacheUser()
                    }
                }
                RetrofitException.Kind.UNAUTHORIZED -> {
                    callback?.startLoginActivity()
                }
                else -> {
                    // no-op
                }
            }
        })
    }

    interface Callback {

        fun startLoginActivity()
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


