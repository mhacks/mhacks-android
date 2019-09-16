package org.mhacks.app.game.widget.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.game.data.model.Score
import org.mhacks.app.game.databinding.ItemGameScoreBinding

class ScoreAdapter
    : ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(ScoreCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ScoreViewHolder(
                    ItemGameScoreBinding.inflate(
                            LayoutInflater.from(parent.context)
                    )
            )

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) =
            holder.bind(position + 1, getItem(position))

    inner class ScoreViewHolder(private val binding: ItemGameScoreBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        fun bind(position: Int, score: Score) = with(binding) {
            itemGameScoreNameTextView.text = score.user
            itemGameScoreOrderTextView.text = position.toString()
            itemGameScorePointsTextView.text = score.score.toString()
        }
    }

    private class ScoreCallback : DiffUtil.ItemCallback<Score>() {

        override fun areItemsTheSame(
                oldItem: Score,
                newItem: Score
        ): Boolean = oldItem.user == newItem.user

        override fun areContentsTheSame(
                oldItem: Score,
                newItem: Score
        ): Boolean = oldItem == newItem
    }
}

