package com.mhacks.android.ui.countdown


import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import com.mhacks.android.data.model.Countdown
import com.mhacks.android.data.network.HackathonCallback
// import com.mhacks.android.data.network.NetworkManager
import com.mhacks.android.ui.common.BaseFragment

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.mhacks.android.R

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * Created by jawad on 04/11/14.
 * Updated by Shashank on 08/30/17
 */

class WelcomeFragment : BaseFragment() {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.welcome
    override var LayoutResourceID: Int = R.layout.fragment_welcome

    private val TAG = "MD/WelcomeFrag"

    // Countdown views
    private var mCircularProgress: ProgressBar? = null
    private var mCountdownTextView: TextView? = null

    // For testing the countdown timer
    private val countdownLength = (10 * 1000).toLong()
    private val countdownUpdateIntervals = (1 * 750).toLong()

    private var startDate: Date? = null
    private var duration: Long = 0

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cache the views that need to be edited later on
        mCircularProgress = view?.findViewById(R.id.progressbar_counter)
        mCountdownTextView = view?.findViewById(R.id.timer_text)
    }

    companion object {
        val instance get() = WelcomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val networkManager = NetworkManager.getInstance()
//        networkManager.getCountdown(object : HackathonCallback<Countdown> {
//            override fun success(response: Countdown) {
//                startDate = Date(response.getStartTime() * 1000)
//                duration = response.getCountdownDuration()
//                initCountdownIfNecessary(startDate, duration * 1000)
//            }
//
//            override fun failure(error: Throwable) {
//
//            }
//        })

        duration = 129600000
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        startDate = simpleDateFormat.parse("2017-09-06T00:00:00.000Z")
        initCountdownIfNecessary(startDate, duration)
        // Start everything off by getting the parse data
        //getLatestParseData();
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

        // Get the endDT in local time
        var localEndDT = DateTime(localStartDT)
        localEndDT = localEndDT.plus(duration - 10800000)

        // Get the current, start, and end times in millis
        val curTime = localDateTime.millis
        val startTime = startDate!!.time
        val endTime = startTime + duration

        Log.d(TAG, "Start Date Orig: " + startDate.toString() + " | " + startDate.time + " | Duration: " + duration)
        Log.d(TAG, "Joda Start Date: " + localStartDT.toString() + " | " + localStartDT.millis)
        Log.d(TAG, "Joda End Date: " + localEndDT.toString() + " | " + localEndDT.millis + " | Supposed: " + endTime)

        // Get a resources reference, to get the necessary display strings
        val res = activity.resources
        // Holds the strings to display

        // Returns date times in the format similar to "DayName, MonthName DD, YYYY at HH:MM AM/PM."
        val dateTimeFormatter = DateTimeFormat.forPattern("EEEE, MMMM d, yyyy 'at' hh:mm a.")

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
            mCircularProgress!!.progress = 100
            mCountdownTextView!!.text = "Done!"
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
        internal var outFormat: DateFormat

        init {

            // Set up the formatter, to display the time remaining prettily later
            outFormat = SimpleDateFormat("HH:mm:ss")
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
            mCountdownTextView!!.text = hrs + ":" + min + (":" + sec).substring(0, 3)

            // Update the progress [maxProgressInt - maxProgressInt*timeRemaining/total time]
            val progress = (100 - 100 * millisUntilFinished / totalHackingTimeInMillis).toInt()
            mCircularProgress!!.progress = progress
        }

        override fun onFinish() {
            mCircularProgress!!.progress = 100
            mCountdownTextView!!.text = "Done!"
        }
    }
}

