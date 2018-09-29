package com.mhacks.app.ui.info.widget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mhacks.app.ui.info.getInfoList
import com.mhacks.app.ui.info.model.Info
import org.mhacks.mhacksui.databinding.ItemInfoBinding

class InfoCardRecyclerViewAdapter
    : RecyclerView.Adapter<
        InfoCardRecyclerViewAdapter.InfoCardViewHolder>() {

    private val infoList by lazy {
        getInfoList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): InfoCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInfoBinding.inflate(inflater, parent, false)
        return InfoCardViewHolder(binding)
    }

    override fun getItemCount() = infoList.size

    override fun onBindViewHolder(viewHolder: InfoCardViewHolder, position: Int) {
        viewHolder.bind(infoList[position])
    }

    class InfoCardViewHolder(private val binding: ItemInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(info: Info) {
            with(binding) {
                itemInfoHeaderTextView.setText(info.header)
                itemInfoIconImageView.setImageResource(info.icon)
                itemInfoSubHeaderTextView.setText(info.subHeader)
                itemInfoDescTextView.setText(info.description)
            }

        }

    }
}