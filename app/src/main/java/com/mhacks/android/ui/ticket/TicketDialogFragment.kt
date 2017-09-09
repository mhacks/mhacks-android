package com.mhacks.android.ui.ticket

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.kotlin.RoomUser
import com.mhacks.android.data.model.Login
import com.mhacks.android.ui.MainActivity
import com.mhacks.android.ui.login.LoginActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_ticket_dialog.*
import net.glxn.qrgen.android.QRCode
import org.mhacks.android.R
import timber.log.Timber

/**
 * Created by jeffreychang on 8/26/17.
 */
class TicketDialogFragment : DialogFragment() {

    private lateinit var key: String
    private val parentActivity by lazy { activity as MainActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        parentActivity.roomSingleton.getLoginO()
//                .flatMap { login ->
//                         parentActivity.networkSingleton
//                                 .getUserObservable(login)
//                                 .toFlowable(BackpressureStrategy.BUFFER)
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (
//                        { response -> Timber.d(response.email) },
//                        { error    -> Timber.d(error.toString())  }
//                )




//        parentActivity.roomSingleton.getLogin(
//                this::onLoginDBSuccess,
//                this::onLoginDBFailure)
    }

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
        key = "JJ"
        val qr = QRCode.from(key)
                .withSize(500, 500)
                .withColor(0xFF43384D.toInt(), 0x00FFFFFF)
                .bitmap()
        ticket_qr_code_image_view.setImageBitmap(qr)
        ticket_bottom_bar_done_button.setOnClickListener { dismiss() }
    }

    private fun onLoginDBSuccess(login: Login) {
        if (login.userSkipped) {
            parentActivity.startActivity(Intent(parentActivity, LoginActivity::class.java))
            parentActivity.finish()
        } else {
//            parentActivity.roomSingleton.getUser(
//                    this::onUserDBSuccess,
//                    this::onUserDBFailure
//            )
        }
    }

    private fun onUserNetworkSuccess(room: RoomUser) {
        Timber.d(room.fullName)
    }

    private fun onUserNetworkFailure(error: Throwable) {
        Timber.e(error)
    }

    private fun onUserDBSuccess(room: RoomUser) {
        Timber.d(room.fullName)
    }

    private fun onUserDBFailure(error: Throwable) {
//        parentActivity.networkSingleton.getUser(
//
//        )
    }

    private fun onLoginDBFailure(error: Throwable) {
        parentActivity.startActivity(Intent(parentActivity, LoginActivity::class.java))
        parentActivity.finish()
    }
    companion object {
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


