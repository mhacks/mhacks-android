package org.mhacks.app.game.widget.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.game.data.model.Player
import org.mhacks.app.game.databinding.ItemGameScoreBinding

class PlayerAdapter
    : ListAdapter<Player, PlayerAdapter.ScoreViewHolder>(ScoreCallback()) {

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

        fun bind(position: Int, player: Player) = with(binding) {
            itemGameLeaderboardNameTextView.text = player.user.full_name
            itemGameLeaderboardOrderTextView.text = position.toString()
            itemGameLeaderboardPointsTextView.text = player.points.toString()
        }
    }

    private class ScoreCallback : DiffUtil.ItemCallback<Player>() {

        override fun areItemsTheSame(
                oldItem: Player,
                newItem: Player
        ): Boolean = oldItem.user == newItem.user

        override fun areContentsTheSame(
                oldItem: Player,
                newItem: Player
        ): Boolean = oldItem == newItem
    }
}

