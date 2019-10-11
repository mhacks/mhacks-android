package org.mhacks.app.game.widget

import android.content.Intent
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
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.mhacks.app.core.Activities
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.showSnackBar
import org.mhacks.app.core.intentTo
import org.mhacks.app.game.GameViewModel
import org.mhacks.app.game.R
import org.mhacks.app.game.databinding.ActivityGameBinding
import org.mhacks.app.game.di.inject
import org.mhacks.app.game.widget.quest.LinearEdgeDecoration
import org.mhacks.app.game.widget.quest.QuestAdapter
import org.mhacks.app.game.widget.player.PlayerAdapter
import timber.log.Timber
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    private var lastSnapPosition: Int = -1

    private lateinit var snapHelper: GravitySnapHelper

    private val questAdapter: QuestAdapter by lazy {
        QuestAdapter()
    }

    private val playerAdapter: PlayerAdapter by lazy {
        PlayerAdapter()
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

                        run {
                            IntentIntegrator(this@GameActivity)
                                    .setBeepEnabled(false)
                                    .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                                    .setPrompt(getString(R.string.scan_prompt))
                                    .setBarcodeImageEnabled(true)
                                    .initiateScan()
                        }
                    }
                    lifecycleOwner = this@GameActivity
                }

        initQuests()
        initScores()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            Timber.d("Captured string: %s", result.contents)
            val quest = questAdapter.getQuest(lastSnapPosition)
            val email = result.contents
            if (quest != null) {
                viewModel.scanQuest(email, quest.question)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
            activityGameLeaderboardRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@GameActivity)
                adapter = playerAdapter
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
            questAdapter.submitList(it.quests)
        })
        gameViewModel.leaderboardLiveData.observe(this, Observer {
            playerAdapter.submitList(it)
        })
        gameViewModel.scanQuestLiveData.observe(this, Observer {
            Toast.makeText(this, "Just scanned a user!", Toast.LENGTH_LONG).show()
        })
        gameViewModel.errorLiveData.observe(this, Observer { error ->

            when (error) {
                RetrofitException.Kind.NETWORK -> {
                    Toast.makeText(this, R.string.game_network_failure, Toast.LENGTH_LONG).show()
                }
                RetrofitException.Kind.UNAUTHORIZED -> {
                    val message = error.name + " - Please sign in to play SiMHacks!"
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    val intent = intentTo(Activities.SignIn)
                    startActivity(intent)
                    finish()
                }

                else -> {
                    Toast.makeText(this, R.string.game_network_failure, Toast.LENGTH_LONG).show()
                }
            }
        })
        gameViewModel.snackBarMessage.observe(this, Observer {
            // it.showSnackBar(binding.root, Snackbar.LENGTH_SHORT)
        })
    }

}

