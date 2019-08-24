package org.mhacks.app.announcements.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.ktx.showSnackBar

import org.mhacks.app.announcements.databinding.FragmentAnnouncementsBinding

import org.mhacks.app.announcements.AnnouncementViewModel
import org.mhacks.app.core.widget.NavigationFragment
import org.mhacks.app.announcements.R
import org.mhacks.app.announcements.di.inject

import javax.inject.Inject
import org.mhacks.app.core.R as coreR


/**
 * Fragment to display and update announcements.
 */
class AnnouncementFragment : NavigationFragment() {

    override var transparentToolbarColor: Int? = null

    override var appBarTitle = R.string.title_announcements

    override var rootView: View? = null

    private var adapter: AnnouncementsAdapter? = null

    private var snackBar: Snackbar? = null

    private lateinit var binding: FragmentAnnouncementsBinding

    @Inject
    lateinit var viewModel: AnnouncementViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
                .apply {
                    inject()
                    subscribeUi(viewModel)
                    viewModel.getAndCacheAnnouncements()
                    lifecycleOwner = this@AnnouncementFragment
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
                        adapter?.updateList(ArrayList(announcements))
                    } else {
                        adapter = AnnouncementsAdapter(context, ArrayList(announcements))

                        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.announcementsRecyclerView.adapter = adapter
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
                    rootView?.showSnackBar(textMessage)
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

