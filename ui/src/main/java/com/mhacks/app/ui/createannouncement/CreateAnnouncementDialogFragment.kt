package com.mhacks.app.ui.createannouncement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mhacks.app.ui.common.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_create_announcements.*
import org.mhacks.mhacksui.databinding.FragmentCreateAnnouncementsBinding

/**
 * Fragment used to create and post a new announcement.
 */
class CreateAnnouncementDialogFragment: BaseDialogFragment() {

    private var isTitleValid = false

    private var isCategoryValid = false

    private var isBodyValid = false

    override var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentCreateAnnouncementsBinding.inflate(
                inflater, container, false).apply {

            dialog?.setTitle("Post announcement")

            val countries = arrayOf("Food", "Emergency", "Event", "Logistics", "Sponsored")
            activity?.let {
                val adapter = ArrayAdapter<String>(it,
                        android.R.layout.simple_dropdown_item_1line, countries)
                fragmentCreateAnnouncementsCategoryEditText.setAdapter(adapter)
            }

            fragmentCreateAnnouncementsCategoryEditText.keyListener = null

            fragmentCreateAnnouncementsCategoryEditText.setOnFocusChangeListener { _, _ ->
                showDropDown()
            }
            fragmentCreateAnnouncementsCategoryEditText.setOnClickListener {
                showDropDown()
            }

            fragmentCreateAnnouncementsTitleEditText.addTextChangedListener(
                    object : TextWatcher {

                        override fun afterTextChanged(p0: Editable?) {
                            isTitleValid = !p0.toString().isEmpty()
                            validate()
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
            fragmentCreateAnnouncementsBodyEditText.addTextChangedListener(
                    object : TextWatcher {

                        override fun afterTextChanged(p0: Editable?) {
                            isBodyValid = !p0.toString().isEmpty()
                            validate()
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
            fragmentCreateAnnouncementsCategoryEditText.setOnItemClickListener {
                _, _, _, _ ->
                isCategoryValid = true
                validate()
            }
            fragmentCreateAnnouncementsPostButton.setOnClickListener {
//                createAnnouncementPresenter.postAnnouncement(
//                        fragment_create_announcements_title_edit_text.text.toString().trim(),
//                        fragment_create_announcements_category_edit_text.text
//                                .toString().toLowerCase().trim(),
//                        fragment_create_announcements_body_edit_text.text.toString().trim()
//                )
            }
            rootView = root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun validate() {
        fragment_create_announcements_post_button.isEnabled =
                isTitleValid and isCategoryValid and isBodyValid
    }

    private fun showDropDown() {
        fragment_create_announcements_category_edit_text.showDropDown()
    }

    companion object {

        val instance
            get() = CreateAnnouncementDialogFragment()
    }
}