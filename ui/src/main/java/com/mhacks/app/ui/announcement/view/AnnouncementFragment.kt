package com.mhacks.app.ui.announcement.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.announcement.AnnouncementViewModel
import com.mhacks.app.ui.common.NavigationBindingFragment
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentAnnouncementsBinding
import javax.inject.Inject

/**
 * Fragment to display and update announcements.
 */
class AnnouncementFragment : NavigationBindingFragment() {

    override var setTransparent = false

    override var appBarTitle = R.string.title_announcements

    override var rootView: View? = null

    private var adapter: AnnouncementsAdapter? = null

    private var snackBar: Snackbar? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        FragmentAnnouncementsBinding.inflate(inflater, container, false)
                .apply {
                    val viewModel = viewModelProvider<AnnouncementViewModel>(viewModelFactory)

                    subscribeUi(viewModel)
                    viewModel.getAndCacheAnnouncements()

                    setLifecycleOwner(this@AnnouncementFragment)
                    rootView = root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(getString(R.string.loading_announcements))
    }

    private fun subscribeUi(announcementViewModel: AnnouncementViewModel) {
        announcementViewModel.announcements.observe(this, Observer { announcements ->
            snackBar?.dismiss()
            snackBar = null
            context?.let { context ->
                announcements?.let {
                    if (adapter != null) {
                        adapter = AnnouncementsAdapter(context, ArrayList(announcements))
                        announcements_recycler_view.adapter = adapter
                        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
                    } else {
                        adapter?.updateList(ArrayList(announcements))
                    }
                }
                showMainContent()
            }

            announcementViewModel.error.observe(this, Observer { error ->
                when (error) {
                    RetrofitException.Kind.NETWORK -> {
                        showErrorView(R.string.announcement_network_failure) {
                            showProgressBar(getString(R.string.loading_announcements))
                        }
                    }
                    else -> {
                        // no-op
                    }
                }
            })


            announcementViewModel.snackbarMessage.observe(this, Observer {
                it?.let { textMessage ->
                    rootView?.showSnackBar(
                            Snackbar.LENGTH_SHORT, textMessage)
                }
            })
        })
    }

    override fun onDetach() {
        super.onDetach()
        snackBar?.dismiss()
    }

    companion object {

        val instance get() = AnnouncementFragment()

    }
}

