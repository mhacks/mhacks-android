package org.mhacks.app.game.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.mhacks.app.game.GameViewModel
import org.mhacks.app.game.R
import org.mhacks.app.game.databinding.ActivityGameBinding
import org.mhacks.app.game.di.inject
import javax.inject.Inject

/**
 * Fragment to display list of events in a viewpager with tabs corresponding to the weekdays.
 */
class GameActivity : AppCompatActivity() {

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
                    lifecycleOwner = this@GameActivity
                }

        viewModel.getQuestions()
    }


    private fun subscribeUi(gameViewModel: GameViewModel) {
        gameViewModel.questionsLiveData.observe(this, Observer {
            it?.let { eventMap ->
            }
        })
    }


}

