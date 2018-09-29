package com.mhacks.app.ui.info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mhacks.app.data.Constants
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.common.SpacingItemDecoration
import com.mhacks.app.ui.info.model.Info
import com.mhacks.app.ui.info.widget.InfoCardRecyclerViewAdapter
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentInfoBinding
import timber.log.Timber
import javax.inject.Inject

class InfoFragment: NavigationFragment() {

    @Inject lateinit var wiFiInstaller: WifiInstaller

    override var setTransparent = false

    override var appBarTitle: Int = R.string.title_info

    override var rootView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoBinding.inflate(
                inflater, container, false).apply {
            context?.let {
                infoFragmentInfoRecyclerView.layoutManager = LinearLayoutManager(
                        it,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
                infoFragmentInfoRecyclerView.addItemDecoration(
                        SpacingItemDecoration(it, 6)
                )

                val adapter = InfoCardRecyclerViewAdapter()
                adapter.infoCallback = ::onItemSelected
                infoFragmentInfoRecyclerView.adapter = adapter
            }
        }
        return binding.root
    }

    private fun onItemSelected(info: Info) {
        Timber.d(info.type.toString())
        when (info.type) {

            Info.TYPE.WIFI -> {
                val isWifiInstall = wiFiInstaller.installConferenceWifi(
                        Constants.WIFI_CONFIGUATION
                )
                context?.let {
                    val wifiMessage = if (isWifiInstall) {
                        R.string.wifi_install_success
                    } else {
                        R.string.wifi_install_failure
                    }
                    Toast.makeText(it, wifiMessage, Toast.LENGTH_SHORT).show()
                }

                Timber.d("Installed network is %s", isWifiInstall.toString())
            }
            Info.TYPE.ADDRESS -> {

            }
            Info.TYPE.SLACK -> {

            }
            Info.TYPE.EMAIL -> {

            }
        }
    }

}