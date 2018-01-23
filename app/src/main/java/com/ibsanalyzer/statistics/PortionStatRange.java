package com.ibsanalyzer.statistics;

/**
 * Created by Johan on 2018-01-23.
 */

public class PortionStatRange {
    //inclusive
    public float rangeStart;

    //exclusive
    public float rangeStop;
    public boolean turnedOn = true;
    PortionStatRange(){

    }

    public PortionStatRange(float rangeStart, float rangeStop, boolean turnedOn) {
        this.rangeStart = rangeStart;
        this.rangeStop = rangeStop;
        this.turnedOn = turnedOn;
    }
}
