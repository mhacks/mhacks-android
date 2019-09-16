package org.mhacks.app.game.widget.quest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.game.R
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.databinding.ItemGameQuestBinding

class QuestAdapter
    : ListAdapter<Quest, QuestAdapter.QuestViewHolder>(QuestionCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            QuestViewHolder(
                    ItemGameQuestBinding.inflate(
                            LayoutInflater.from(parent.context)
                    )
            )

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) =
            holder.bind(getItem(position))

    fun getQuest(position: Int): Quest? {
        return try {
            getItem(position)
        } catch (e: Exception) {
            null
        }
    }

    inner class QuestViewHolder(private val binding: ItemGameQuestBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        fun bind(quest: Quest) = with(binding) {
            itemGameQuestTextView.text = quest.quests
            itemGameQuestPointsTextView.text = root.context.getString(
                    R.string.game_points,
                    quest.points.toString()
            )
        }
    }

    private class QuestionCallback : DiffUtil.ItemCallback<Quest>() {
        override fun areItemsTheSame(
                oldItem: Quest,
                newItem: Quest
        ): Boolean = oldItem.quests == newItem.quests

        override fun areContentsTheSame(
                oldItem: Quest,
                newItem: Quest
        ): Boolean = oldItem == newItem
    }
}

