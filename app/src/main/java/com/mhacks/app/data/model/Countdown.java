package com.mhacks.app.data.model;

/**
 * Created by omkarmoghe on 10/3/2016.
 */
public class Countdown {
    public long dateUpdate;
    public long startTime;
    public int hacksSubmitted;
    public long countdownDuration;

    public Countdown() {
    }

    public long getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(long dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getHacksSubmitted() {
        return hacksSubmitted;
    }

    public void setHacksSubmitted(int hacksSubmitted) {
        this.hacksSubmitted = hacksSubmitted;
    }

    public long getCountdownDuration() {
        return countdownDuration;
    }

    public void setCountdownDuration(long countdownDuration) {
        this.countdownDuration = countdownDuration;
    }
}
