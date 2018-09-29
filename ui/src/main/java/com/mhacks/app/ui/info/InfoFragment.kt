package com.mhacks.app.ui.info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.ui.common.SpacingItemDecoration
import com.mhacks.app.ui.info.view.InfoCardRecyclerViewAdapter
import org.mhacks.mhacksui.databinding.FragmentInfoBinding

class InfoFragment: Fragment() {

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
                infoFragmentInfoRecyclerView.adapter = InfoCardRecyclerViewAdapter()
            }
        }
        return binding.root
    }

}