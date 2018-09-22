package com.mhacks.app.ui.ticket.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.mhacksui.R
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.ticket.TicketViewModel
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_ticket_dialog.*
import net.glxn.qrgen.android.QRCode
import org.mhacks.mhacksui.databinding.FragmentTicketDialogBinding
import javax.inject.Inject

/**
 * Fragment to display user information and show their QR code
 */
class TicketDialogFragment : DaggerAppCompatDialogFragment() {

    @Inject lateinit var ticketViewModel: TicketViewModel

    @Inject lateinit var authInterceptor: AuthModule.AuthInterceptor

    private var callback: Callback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as? Callback
    }

    override fun onResume() {
        val width = (resources.displayMetrics.widthPixels * .85).toInt()
        val height = (resources.displayMetrics.heightPixels * .7).toInt()

        dialog.window?.setLayout(width, height)
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val binding = FragmentTicketDialogBinding.inflate(
                inflater, container, false).apply {
            ticketBottomBarDoneButton.setOnClickListener {
                dismiss()
            }
            subscribeUi(ticketViewModel, this)
            ticketViewModel.getAndCacheUser()
            setLifecycleOwner(this@TicketDialogFragment)
        }

        return binding.root
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
                    showErrorView()
                    binding.ticketErrorView.tryAgainCallback = {
                        showProgressBar()
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
    
    private fun showProgressBar() {
        ticket_progressbar.visibility = View.VISIBLE
        ticket_main.visibility = View.INVISIBLE
        ticket_error_view.visibility = View.INVISIBLE
    }

    private fun showMainContent() {
        ticket_progressbar.visibility = View.INVISIBLE
        ticket_main.visibility = View.VISIBLE
        ticket_error_view.visibility = View.INVISIBLE
    }

    private fun showErrorView() {
        ticket_error_view.removeBackground()
        ticket_error_view.titleText = R.string.ticket_network_error
        ticket_error_view.iconDrawable = R.drawable.ic_cloud_off_black_24dp
        ticket_error_view.textColor = R.color.colorPrimaryDark
        ticket_progressbar.visibility = View.INVISIBLE
        ticket_main.visibility = View.INVISIBLE
        ticket_error_view.visibility = View.VISIBLE
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


