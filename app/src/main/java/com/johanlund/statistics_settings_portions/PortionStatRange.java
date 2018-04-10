package com.johanlund.statistics_settings_portions;

/**
 * Created by Johan on 2018-01-23.
 */

public class PortionStatRange {
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
}
