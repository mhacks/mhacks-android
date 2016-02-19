package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 2/18/2016.
 */
public class Countdown {
    public long startTime;
    public long countdownDuration;

    public Countdown() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCountdownDuration() {
        return countdownDuration;
    }

    public void setCountdownDuration(long countdownDuration) {
        this.countdownDuration = countdownDuration;
    }
}
