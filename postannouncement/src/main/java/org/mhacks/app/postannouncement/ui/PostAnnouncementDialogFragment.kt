package org.mhacks.app.postannouncement.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.BaseDialogFragment
import org.mhacks.app.postannouncement.PostAnnouncementViewModel
import org.mhacks.app.postannouncement.R
import org.mhacks.app.postannouncement.data.model.PostAnnouncement
import org.mhacks.app.postannouncement.databinding.FragmentPostAnnouncementBinding
import org.mhacks.app.postannouncement.inject
import javax.inject.Inject

/**
 * Fragment used to create and post a new announcement.
 */
class PostAnnouncementDialogFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentPostAnnouncementBinding

    private var isTitleValid = false

    private var isCategoryValid = false

    private var isBodyValid = false

    override var rootView: View? = null

    @Inject
    lateinit var postAnnouncementViewModel: PostAnnouncementViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        inject()
        binding = FragmentPostAnnouncementBinding.inflate(
                inflater, container, false).apply {

            dialog?.setTitle(context?.getString(R.string.post_announcement))

            val countries = arrayOf(
                    "Food",
                    "Emergency",
                    "Event",
                    "Logistics",
                    "Sponsored")

            activity?.let {
                val adapter = ArrayAdapter(it,
                        android.R.layout.simple_dropdown_item_1line, countries)
                fragmentCreateAnnouncementCategoryEditText.setAdapter(adapter)
            }

            fragmentCreateAnnouncementCategoryEditText.keyListener = null

            fragmentCreateAnnouncementCategoryEditText.setOnFocusChangeListener { _, _ ->
                showDropDown()
            }
            fragmentCreateAnnouncementCategoryEditText.setOnClickListener {
                showDropDown()
            }

            fragmentCreateAnnouncementTitleEditText.addTextChangedListener(
                    object : TextWatcher {

                        override fun afterTextChanged(p0: Editable?) {
                            isTitleValid = !p0.toString().isEmpty()
                            validate()
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
            fragmentCreateAnnouncementBodyEditText.addTextChangedListener(
                    object : TextWatcher {

                        override fun afterTextChanged(p0: Editable?) {
                            isBodyValid = p0.toString().isNotEmpty()
                            validate()
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
            fragmentCreateAnnouncementCategoryEditText.setOnItemClickListener { _, _, _, _ ->
                isCategoryValid = true
                validate()
            }

            subscribeUi(postAnnouncementViewModel)

            fragmentCreateAnnouncementPostButton.setOnClickListener {
                postAnnouncementViewModel.postAnnouncement(
                        PostAnnouncement(
                                fragmentCreateAnnouncementTitleEditText.text.toString().trim(),
                                fragmentCreateAnnouncementBodyEditText.text
                                        .toString().trim(),
                                fragmentCreateAnnouncementCategoryEditText.text
                                        .toString().toLowerCase().trim(),
                                true, isSent = true, push = true
                        )
                )
            }
            rootView = root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeUi(postAnnouncementViewModel: PostAnnouncementViewModel) {
        postAnnouncementViewModel.createAnnouncement.observe(this, Observer {
            Toast.makeText(
                    context,
                    R.string.post_announcement_success,
                    Toast.LENGTH_LONG)
                    .show()
            dismiss()

        })
        postAnnouncementViewModel.text.observe(this, Observer {
            binding.root.showSnackBar(it)
        })
    }

    private fun validate() {
        binding.fragmentCreateAnnouncementPostButton.isEnabled =
                isTitleValid and isCategoryValid and isBodyValid
    }

    private fun showDropDown() {
        binding.fragmentCreateAnnouncementCategoryEditText.showDropDown()
    }

}