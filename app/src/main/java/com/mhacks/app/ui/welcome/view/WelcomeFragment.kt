package com.mhacks.app.ui.welcome.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Configuration
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.welcome.presenter.WelcomeFragmentPresenter
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * The first screen that the user will open once they are logged in.
 *
 * Manages a ProgressBar that acts as a timer as well as builds
 */

class WelcomeFragment : NavigationFragment(), WelcomeView {

    override var setTransparent: Boolean = false

    override var appBarTitle: Int = R.string.welcome

    override var layoutResourceID: Int = R.layout.fragment_welcome

    private var timer: HackingCountdownTimer? = null

    @Inject lateinit var welcomePresenter: WelcomeFragmentPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomePresenter.getConfig()
    }


    override fun onGetConfigSuccess(config: Configuration) =
        initCountdownIfNecessary(config.startDateTs, config.endDateTs)

    override fun onGetConfigFailure(error: Throwable) {
        val duration = 129600000L
        val startDate = LocalDateTime.parse(FIXED_START_DATE)
                .toEpochSecond(ZoneOffset.UTC)
        initCountdownIfNecessary(startDate, duration)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        welcomePresenter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        welcomePresenter.onDetach()
        timer = null
    }

    /**
     * Initializes the countdown based off of the start date and duration if necessary
     *
     * @param [startDateTs]
     * When this countdown is supposed to start
     *
     * @param [endDateTs]
     * When this countdown is supposed to end
     */
    private fun initCountdownIfNecessary(startDateTs: Long, endDateTs: Long) {

        val startDate = Instant.ofEpochSecond(startDateTs)
                .atZone(ZoneId.systemDefault()).toLocalDateTime()
        val duration = endDateTs - startDateTs

        val localDateTime = LocalDateTime.now()

        val curTime = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val startTime = startDate!!.toEpochSecond(ZoneOffset.UTC)
        val endTime = startTime + duration

        when {
            curTime < startDate.toEpochSecond(ZoneOffset.UTC) ->
                timer_text.text = getString(R.string.countdown_timer_default)
            curTime < endTime -> {
                // Calculate the time remaining and the total time of hacking
                val timeRemaining = endTime - curTime
                val totalHackingTime = endTime - startTime

                // Start the countdown timer
                timer = HackingCountdownTimer(timeRemaining, totalHackingTime)
                timer?.start()
            }
            else -> {
                progressbar_counter.progress = 100
                timer_text.text = getString(R.string.done)
            }
        }
    }

    /**
     * @param[totalHackingTimeInMillis]
     *
     * Total amount of hacking time in milliseconds.
     **/
    private inner class HackingCountdownTimer(
            millisInFuture: Long, internal var totalHackingTimeInMillis: Long) :
            CountDownTimer(millisInFuture, countdownUpdateIntervals) {
        internal var outFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        init {
            outFormat.timeZone = TimeZone.getTimeZone("UTC")
        }

        override fun onTick(millisUntilFinished: Long) {
            val hours = millisUntilFinished / 3600000
            val minutes = (millisUntilFinished - hours * 3600000) / 60000
            val seconds = millisUntilFinished - hours * 3600000 - minutes * 60000

            /**
             * In the case of there being a single digit during the countdown.
             *     e.g. 12:9:01
             *
             * This code would pad the text so it would be 12:09:01
             */
            val hrs = if (hours < 10) "0" + hours.toString() else hours.toString()
            val min = if (minutes < 10) "0" + minutes.toString() else minutes.toString()
            val sec = (if (seconds < 10) "0" + seconds.toString() else seconds.toString())
                    .substring(0, 2)

            timer_text?.text = String.format(getString(R.string.timer_countdown_text), hrs, min, sec)

            val progress = (100 - (100 * millisUntilFinished) / totalHackingTimeInMillis).toInt()
            progressbar_counter?.progress = progress
        }

        override fun onFinish() {
            progressbar_counter?.progress = 100
            timer_text?.text = getString(R.string.done)
        }
    }

    companion object {

        val instance get() = WelcomeFragment()

        private const val countdownUpdateIntervals = 750L

        private const val FIXED_START_DATE = "2017-09-23T00:00:00.000Z"
    }
}
