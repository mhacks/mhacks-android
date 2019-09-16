package org.mhacks.app.game.widget

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import org.mhacks.app.game.GameViewModel
import org.mhacks.app.game.R
import org.mhacks.app.game.databinding.ActivityGameBinding
import org.mhacks.app.game.di.inject
import org.mhacks.app.game.widget.quest.LinearEdgeDecoration
import org.mhacks.app.game.widget.quest.QuestAdapter
import org.mhacks.app.game.widget.score.ScoreAdapter
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    private var lastSnapPosition: Int = -1

    private lateinit var snapHelper: GravitySnapHelper

    private val questAdapter: QuestAdapter by lazy {
        QuestAdapter()
    }

    private val scoreAdapter: ScoreAdapter by lazy {
        ScoreAdapter()
    }

    private lateinit var binding: ActivityGameBinding

    @Inject
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        binding = DataBindingUtil.setContentView<ActivityGameBinding>(
                this,
                R.layout.activity_game
        )
                .apply {
                    subscribeUi(viewModel)
                    activityGameQuestionsScanButton.setOnClickListener {
                        val quest = questAdapter.getQuest(lastSnapPosition)
                        viewModel.scanQuest(quest)
                    }
                    lifecycleOwner = this@GameActivity
                }

        initQuests()
        initScores()
    }

    private fun initQuests() {
        binding.apply {
            snapHelper = GravitySnapHelper(Gravity.CENTER)
            activityGameQuestionsRecyclerView.apply {
                snapHelper.attachToRecyclerView(this)
                snapHelper.setSnapListener {
                    lastSnapPosition = it
                }
                layoutManager =
                        LinearLayoutManager(
                                this@GameActivity,
                                RecyclerView.HORIZONTAL,
                                false
                        )
                clipChildren = false
                addItemDecoration(
                        LinearEdgeDecoration(
                                startPadding = resources.getDimensionPixelOffset(
                                        R.dimen.extra_padding
                                ),
                                endPadding = resources.getDimensionPixelOffset(
                                        R.dimen.extra_padding
                                ),
                                orientation = RecyclerView.HORIZONTAL
                        )
                )
                adapter = questAdapter
            }
        }
    }

    private fun initScores() {
        binding.apply {
            activityGameScoresRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@GameActivity)
                adapter = scoreAdapter
                addItemDecoration(
                        DividerItemDecoration(
                                this@GameActivity,
                                RecyclerView.VERTICAL
                        ).apply {
                            getDrawable(R.drawable.line_divider)?.let {
                                setDrawable(it)
                            }
                        })
            }
        }
    }

    private fun subscribeUi(gameViewModel: GameViewModel) {
        gameViewModel.questLiveData.observe(this, Observer {
            questAdapter.submitList(it)
        })
        gameViewModel.scoresLiveData.observe(this, Observer {
            scoreAdapter.submitList(it)
        })
        gameViewModel.errorLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

}

