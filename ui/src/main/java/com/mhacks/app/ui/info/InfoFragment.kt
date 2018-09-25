package com.mhacks.app.ui.info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.mhacksui.databinding.FragmentInfoBinding

class InfoFragment: Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoBinding.inflate(inflater, container, false).apply {

        }
        return binding.root
    }



}