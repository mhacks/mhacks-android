package com.mhacks.app.ui.welcome.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.MetaConfiguration
import com.mhacks.app.ui.common.BaseFragment
import com.mhacks.app.ui.welcome.presenter.WelcomeFragmentPresenter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by jawad on 04/11/14.
 * Updated by Shashank on 08/30/17
 */

class WelcomeFragment : BaseFragment(), WelcomeView {

    override var setTransparent: Boolean = false
    override var appBarTitle: Int = R.string.welcome
    override var layoutResourceID: Int = R.layout.fragment_welcome

    private var circularProgress: ProgressBar? = null
    private var countdownTextView: TextView? = null

    private val countdownLength = (10 * 1000).toLong()
    private val countdownUpdateIntervals = (1 * 750).toLong()

    private var startDate: Date? = null
    private var duration: Long = 0

    @Inject lateinit var welcomePresenter: WelcomeFragmentPresenter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomePresenter.onViewLoad()
    }

    override fun onViewLoaded() {
        circularProgress = view?.findViewById(R.id.progressbar_counter)
        countdownTextView = view?.findViewById(R.id.timer_text)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        duration = 129600000
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("America/Detroit")
        startDate = simpleDateFormat.parse("2017-09-23T00:00:00.000Z")
        initCountdownIfNecessary(startDate, duration)

    }

    private fun onConfigCallbackSuccess(config: MetaConfiguration) {

        startDate = Date(config.configuration.startDateTs)
        Timber.d(config.configuration.startDate)
        duration = config.configuration.endDateTs - config.configuration.startDateTs
        initCountdownIfNecessary(startDate, duration)

    }

    private fun onConfigCallbackFailure(error: Throwable) {
        Timber.e(error.message)
    }

    /**
     * Initializes the countdown based off of the start date and duration if necessary
     * @param startDate - Date(+time) this countdown is supposed to start
     * @param duration - How long from this start date this countdown timer should end, in milliseconds
     */
    private fun initCountdownIfNecessary(startDate: Date?, duration: Long) {
        // Get the local date+time
        val localDateTime = DateTime()

        // Get the local date time zone to convert DT's to local times
        val localTZID = TimeZone.getDefault().id
        val localDTZ = DateTimeZone.forID(localTZID)

        // Get the start date in local time zone
        val localStartDT = DateTime(startDate) // Get the startDate in joda time library in EST tz
        localStartDT.toDateTime(localDTZ)

        // Get the current, start, and end times in millis
        val curTime = localDateTime.millis
        val startTime = startDate!!.time
        val endTime = startTime + duration

        // Holds the strings to display

        if (curTime < startTime) {
            // If so, it's not hack time just yet

        } else if (curTime < endTime) {
            // If so, hacking already started

            // Calculate the time remaining and the total time of hacking
            val timeRemaining = endTime - curTime
            val totalHackingTime = endTime - startTime

            // Start the countdown timer
            val timer = HackingCountdownTimer(timeRemaining, totalHackingTime)
            timer.start()
        } else {
            // Otherwise, hacking already ended =<

            // Set the counter to its "finished" state
            circularProgress!!.progress = 100
            countdownTextView!!.text = "Done!"
        }
    }

    private inner class HackingCountdownTimer
    /** DEPRECATED but here for reference
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
    //        public HackingCountdownTimer(long millisInFuture, long countDownInterval) {
    //            super(millisInFuture, countDownInterval);
    //        }

    (millisInFuture: Long, // Cached total amount of hacking time in milliseconds, to update the progress circle
     internal var totalHackingTimeInMillis: Long) : CountDownTimer(millisInFuture, countdownUpdateIntervals) {
        // Used to display the time remaining prettily
        internal var outFormat: DateFormat = SimpleDateFormat("HH:mm:ss")

        init {

            // Set up the formatter, to display the time remaining prettily later
            outFormat.timeZone = TimeZone.getTimeZone("UTC")
        }// Cache the total hacking time to determine progress later

        override fun onTick(millisUntilFinished: Long) {
            val hours = millisUntilFinished / 3600000
            val minutes = (millisUntilFinished - hours * 3600000) / 60000
            val seconds = millisUntilFinished - hours * 3600000 - minutes * 60000

            // Padding hrs, mins, and secs to prevent out of range on substring & to improve ux
            val hrs: String
            val min: String
            val sec: String
            hrs = if (hours < 10) "0" + hours.toString() else hours.toString()
            min = if (minutes < 10) "0" + minutes.toString() else minutes.toString()
            sec = if (seconds < 10) "0" + seconds.toString() else seconds.toString()

            // Update the countdown timer textView
            countdownTextView!!.text = hrs + ":" + min + (":" + sec).substring(0, 3)

            // Update the progress [maxProgressInt - maxProgressInt*timeRemaining/total time]
            val progress = (100 - 100 * millisUntilFinished / totalHackingTimeInMillis).toInt()
            circularProgress?.progress = progress
        }

        override fun onFinish() {
            circularProgress?.progress = 100
            countdownTextView?.text = "Done!"
        }
    }

    interface Callback {
        fun checkOrFetchConfig(success: (config: MetaConfiguration) -> Unit,
                               failure: (error: Throwable) -> Unit)
    }

    companion object {
        val instance get() = WelcomeFragment()
    }
}