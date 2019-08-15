package org.mhacks.app.ticket

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import net.glxn.qrgen.android.QRCode
import org.mhacks.app.core.callback.TicketDialogCallback
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.BaseDialogFragment
import org.mhacks.app.ticket.databinding.FragmentTicketDialogBinding
import javax.inject.Inject

class TicketDialogFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentTicketDialogBinding

    @Inject
    lateinit var ticketViewModel: TicketViewModel

    private var callback: TicketDialogCallback? = null

    override var rootView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as? TicketDialogCallback
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        inject()
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentTicketDialogBinding.inflate(inflater, container, false)
                .apply {
                    fragmentTicketBottomBarDoneButton.setOnClickListener {
                        dismiss()
                    }
                    subscribeUi(ticketViewModel)

                    ticketViewModel.getAndCacheUser()
                    lifecycleOwner = this@TicketDialogFragment

                    setParentBackground(R.drawable.ticket_background)
                    rootView = root
                }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(R.string.loading_ticket)
    }

    private fun subscribeUi(ticketViewModel: TicketViewModel) {
        ticketViewModel.user.observe(this, Observer {
            it?.let { user ->
                val qr = QRCode.from(user.email)
                        .withSize(500, 500)
                        .withColor(0xFF43384D.toInt(), 0x00FFFFFF)
                        .bitmap()
                binding.fragmentTicketQrCodeImageView.setImageBitmap(qr)
                binding.fragmentTicketFullNameTextView.text = user.fullName
                if (user.university.isNullOrEmpty()) {
                    binding.fragmentTicketSchoolTextView.text = getString(R.string.no_school)
                } else {
                    binding.fragmentTicketSchoolTextView.text = user.university
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
                    callback?.onTicketUnauthorized()
                }
                else -> {
                    // no-op
                }
            }
        })
        ticketViewModel.snackBarMessage.observe(this, Observer { textMessage ->
            rootView?.showSnackBar(textMessage)
        })
    }
}


