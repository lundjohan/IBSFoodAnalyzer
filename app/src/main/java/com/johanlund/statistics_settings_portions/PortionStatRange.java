package com.johanlund.statistics_settings_portions;

import android.support.annotation.NonNull;

/**
 * Created by Johan on 2018-01-23.
 */

public class PortionStatRange implements Comparable<PortionStatRange>{
    //inclusive
    private float rangeStart;

    //exclusive
    private float rangeStop;
    private boolean turnedOn = true;
    PortionStatRange(){

    }

    public PortionStatRange(float rangeStart, float rangeStop, boolean turnedOn) {
        this.rangeStart = rangeStart;
        this.rangeStop = rangeStop;
        this.turnedOn = turnedOn;
    }

    public float getRangeStart() {
        return rangeStart;
    }

    public float getRangeStop() {
        return rangeStop;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    @Override
    public String toString(){
        return String.format("%.1f", rangeStart) + "-" + String.format("%.1f", rangeStop);


    }

    @Override
    public int compareTo(@NonNull PortionStatRange p2) {
        int startDiff = (int)((rangeStart - p2.getRangeStart())*100);
        if (startDiff == 0){
            return (int)((rangeStop - p2.getRangeStop())*100);
        }
        return startDiff;
    }
}
