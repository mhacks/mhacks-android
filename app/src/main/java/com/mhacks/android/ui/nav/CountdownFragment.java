package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mhacks.android.ui.MainActivity;
import com.mhacks.iv.android.R;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    // Tags for getting parse data
    private static final String TAG_COUNTDOWN_STARTDATE = "countdownStartDate";
    private static final String TAG_COUNTDOWN_DURATION = "countdownDuration";

    // Countdown views
    private ProgressBar mCircularProgress;
    private TextView mCountdownTextView;

    // Title textViews
    TextView mTopTitleText, mTopTimeText, mBottomTitleText, mBottomTimeText;

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

        mTopTitleText = (TextView) view.findViewById(R.id.countdown_toptitle_intro);
        mTopTimeText = (TextView) view.findViewById(R.id.countdown_toptitle_time);
        mBottomTitleText = (TextView) view.findViewById(R.id.countdown_bottomtitle_intro);
        mBottomTimeText = (TextView) view.findViewById(R.id.countdown_bottomtitle_time);

        // TEST
/*        Button button = (Button) view.findViewById(R.id.button);
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
        getLatestParseData();
    }

    // Gets the parse data to start things off
    private void getLatestParseData() {
        // First try for the local data to show something to the user
        ParseConfig localConfig = ParseConfig.getCurrentConfig();
        Date startDate = localConfig.getDate(TAG_COUNTDOWN_STARTDATE);
        long duration = localConfig.getLong(TAG_COUNTDOWN_DURATION);

        final boolean canUseLocalData = (startDate != null); // Kinda weird
        if(canUseLocalData) {
            if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
            initCountdownIfNecessary(startDate, duration * 1000);
        }

        // Then get the remote data
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if (e != null || parseConfig == null) {
                    Log.e(TAG, "Failed to fetch. Using Cached Config if it's available.");

                    // If we don't have any local data, let the user know we need internet
                    if(!canUseLocalData) {
                        if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                    }

                    return;
                }

                // Get the date and duration from the config
                Date startDate = parseConfig.getDate(TAG_COUNTDOWN_STARTDATE);
                long duration = parseConfig.getLong(TAG_COUNTDOWN_DURATION);

                if(startDate == null && !canUseLocalData) {
                    // If something was wrong with the remote data and we don't have local data, let the user know
                    if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                } else {
                    // Hey check it out something went right
                    if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                    initCountdownIfNecessary(startDate, duration * 1000);
                }
            }
        });
    }

    /**
     * Initializes the countdown based off of the start date and duration if necessary
     * @param startDate - Date(+time) this countdown is supposed to start
     * @param duration - How long from this start date this countdown timer should end, in milliseconds
     */
    private void initCountdownIfNecessary(Date startDate, long duration) {
        // Get the local date+time
        DateTime localDateTime = new DateTime();

        // Get the local date time zone to convert DT's to local times
        String localTZID = TimeZone.getDefault().getID();
        DateTimeZone localDTZ = DateTimeZone.forID(localTZID);

        // Get the start date in local time zone
        DateTime localStartDT = new DateTime(startDate); // Get the startDate in joda time library in EST tz
        localStartDT.toDateTime(localDTZ);

        // Get the endDT in local time
        DateTime localEndDT = new DateTime(localStartDT);
        localEndDT = localEndDT.plus(duration);

        // Get the current, start, and end times in millis
        long curTime = localDateTime.getMillis();
        long startTime = startDate.getTime();
        long endTime = startTime + duration;

        Log.d(TAG, "Start Date Orig: " + startDate.toString() + " | " + startDate.getTime() + " | Duration: " + duration);
        Log.d(TAG, "Joda Start Date: " + localStartDT.toString() + " | " + localStartDT.getMillis());
        Log.d(TAG, "Joda End Date: " + localEndDT.toString() + " | " + localEndDT.getMillis() + " | Supposed: " + endTime);

        // Get a resources reference, to get the necessary display strings
        Resources res = getResources();
        // Holds the strings to display
        String topTitle, topTime = null, bottomTitle, bottomTime = null;

        // Returns date times in the format similar to "DayName, MonthName DD, YYYY at HH:MM AM/PM."
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE, MMMM d, yyyy 'at' HH:mm a.");

        if(curTime < startTime) {
            // If so, it's not hack time just yet
            topTitle = res.getString(R.string.countdown_toptitle_beforestart);
            bottomTitle = res.getString(R.string.countdown_bottomtitle_normal);

            topTime = dateTimeFormatter.print(localStartDT);
            bottomTime = dateTimeFormatter.print(localEndDT);
        }
        else if (curTime < endTime) {
            // If so, hacking already started
            topTitle = res.getString(R.string.countdown_toptitle_normal);
            bottomTitle = res.getString(R.string.countdown_bottomtitle_normal);

            topTime = dateTimeFormatter.print(localStartDT);
            bottomTime = dateTimeFormatter.print(localEndDT);

            // Calculate the time remaining and the total time of hacking
            long timeRemaining = endTime-curTime;
            long totalHackingTime = endTime - startTime;

            // Start the countdown timer
            HackingCountdownTimer timer = new HackingCountdownTimer(timeRemaining, totalHackingTime);
            timer.start();
        }
        else {
            // Otherwise, hacking already ended =<
            topTitle = res.getString(R.string.countdown_toptitle_normal);
            bottomTitle = res.getString(R.string.countdown_bottomtitle_done);

            topTime = dateTimeFormatter.print(localStartDT);
            bottomTime = dateTimeFormatter.print(localEndDT);

            // Set the counter to its "finished" state
            mCircularProgress.setProgress(100);
            mCountdownTextView.setText("Done!");
        }

        // Display the Strings in their respective TextViews
        mTopTitleText.setText(topTitle);
        mTopTimeText.setText(topTime);
        mBottomTitleText.setText(bottomTitle);
        mBottomTimeText.setText(bottomTime);
    }

    private class HackingCountdownTimer extends CountDownTimer {
        // Used to display the time remaining prettily
        DateFormat outFormat;
        // Cached total amount of hacking time in milliseconds, to update the progress circle
        long totalHackingTimeInMillis;

        /** DEPRECATED but here for reference
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
//        public HackingCountdownTimer(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }

        public HackingCountdownTimer(long millisInFuture, long totalHackingTimeInMillis) {
            super(millisInFuture, countdownUpdateIntervals);

            // Cache the total hacking time to determine progress later
            this.totalHackingTimeInMillis = totalHackingTimeInMillis;

            // Set up the formatter, to display the time remaining prettily later
            outFormat = new SimpleDateFormat("HH:mm:ss");
            outFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Put the time remaining prettily into the HH:mm:ss format
            Date d = new Date(millisUntilFinished);
            String result = outFormat.format(d);

            // Update the countdown timer textView
            mCountdownTextView.setText(result);

            // Update the progress [maxProgressInt - maxProgressInt*timeRemaining/total time]
            int progress = (int) (100 - 100*millisUntilFinished/totalHackingTimeInMillis);
            mCircularProgress.setProgress(progress);
        }

        @Override
        public void onFinish() {
            mCircularProgress.setProgress(100);
            mCountdownTextView.setText("Done!");
        }
    }
}
