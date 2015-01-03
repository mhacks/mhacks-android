package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mhacks.android.ui.common.Util;
import com.mhacks.iv.android.R;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by jawad on 04/11/14.
 */
/* TODO List
 * 1) Get data from parse
 * 2) Create countdown object+layout+settings
 * 3) Apply data/time to countdown
 */
public class CountdownFragment extends Fragment  {
    private static final String TAG = "MD/CountdownFrag";

    private ProgressBar mCircularProgress;
    private TextView mCountdownTextView;

    // TextViews for displaying when hacking starts+ends
    private TextView mTopTitleTextView, mTopTimeTextView;
    private TextView mBottomTitleTextView, mBottomTimeTextView;

    // Tags for getting parse data
    private static final String TAG_COUNTDOWN_STARTDATE = "countdownStartDate";
    private static final String TAG_COUNTDOWN_DURATION = "countdownDuration";

    // For testing the countdown timer
    private final long countdownLength = 10 * 1000;
    private final long countdownUpdateIntervals = 1*500;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);

        // Cache the views that need to be edited later on
        mCircularProgress = (ProgressBar) view.findViewById(R.id.progressbar_counter);
        mCountdownTextView = (TextView) view.findViewById(R.id.timer_text);

        // Cache the necessary TextViews which display the starting/ending times
        mTopTitleTextView = (TextView) view.findViewById(R.id.countdown_toptitle_intro);
        mTopTimeTextView = (TextView) view.findViewById(R.id.countdown_toptitle_time);
        mBottomTitleTextView = (TextView) view.findViewById(R.id.countdown_bottomtitle_intro);
        mBottomTimeTextView = (TextView) view.findViewById(R.id.countdown_bottomtitle_time);

/*        // FOR TESTING- code kept here for testing the timer if necessary
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int progress = random.nextInt(100);
                mCircularProgress.setProgress(progress);

                HackingCountdownTimer timer = new HackingCountdownTimer(countdownLength, countdownUpdateIntervals);
                timer.start();
            }
        });*/

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Start everything off by getting the parse data
        initParseData();
    }

    // Gets the parse data to start things off
    private void initParseData() {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Failed to fetch. Using Cached Config.");
                    parseConfig = ParseConfig.getCurrentConfig();
                }

                // Get the date and duration from the config
                Date startDate = parseConfig.getDate(TAG_COUNTDOWN_STARTDATE);
                long duration = parseConfig.getLong(TAG_COUNTDOWN_DURATION);

                Toast.makeText(getActivity(), "Date: " + startDate.toString()
                        + " | Duration: " + duration, Toast.LENGTH_SHORT).show();

                initCountdown(startDate, duration);
            }
        });
    }

    /**
     * Initializes the countdown based off of the start date and duration
     * @param startDate - Date this countdown is supposed to start
     * @param duration - How long from this start date this countdown timer should end
     */
    private void initCountdown(Date startDate, long duration) {
        // Get the current time to compare against the start time
        DateTimeZone easternTimeZone = DateTimeZone.forID("America/Detroit");
        DateTime curDateTime = new DateTime(easternTimeZone);

        // Get the start and end times, in milliseconds
        long startTime = startDate.getTime();
        long endTime = startTime + duration;

        Log.d(TAG, "StartDate: " + startDate.toString() + " | Duration: " + duration);
        Log.d(TAG, "Cur Time: " + curDateTime.getMillis() + " | Date: " + curDateTime.toString());
        Log.d(TAG, "Start Time: " + startTime + " | Date: " + new Date(startTime).toString());
        Log.d(TAG, "End Time: " + endTime + " Date: " + new Date(endTime).toString());

        // Get a reference to the Resources in order to set the displayed texts
        Resources resources = getResources();
        // The strings to set for the TextViews
        String topTitle, topTime = null, bottomTitle, bottomTime = null;

        // If the curTime < startTime, then countdown shouldn't start yet
        if(curDateTime.getMillis() < startTime) {
            // Get the title strings to display
            topTitle = resources.getString(R.string.countdown_toptitle_beforestart);
            bottomTitle = resources.getString(R.string.countdown_bottomtitle_normal);

            // Get the default time strings to display (no need to try formatting time)
            topTime = resources.getString(R.string.countdown_toptitle_timedefault);
            bottomTime = resources.getString(R.string.countdown_bottomtitle_timedefault);

            // TODO: Create a personal countdown to start the countdown
        }
        // Then if curTime is less than the end time, start the countdown!
        else if(curDateTime.getMillis() < endTime) {
            // Get the title strings to display
            topTitle = resources.getString(R.string.countdown_toptitle_normal);
            bottomTitle = resources.getString(R.string.countdown_bottomtitle_normal);

            //
        }
        // Otherwise, hacking already ended!
        else {
            // Get the title strings to display
            topTitle = resources.getString(R.string.countdown_toptitle_normal);
            bottomTitle = resources.getString(R.string.countdown_bottomtitle_done);

            //
        }

        // Set the strings onto the appropriate TextViews
        mTopTitleTextView.setText(topTitle);
        mTopTimeTextView.setText(topTime);
        mBottomTitleTextView.setText(bottomTitle);
        mBottomTimeTextView.setText(bottomTime);
    }

    private class HackingCountdownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public HackingCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Update the countdown timer
            mCountdownTextView.setText("" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            mCountdownTextView.setText("Done!");
        }
    }
}
