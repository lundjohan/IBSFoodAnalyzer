package com.ibsanalyzer.util;

/**
 * Created by Johan on 2017-05-28.
 * <p>
 * Simple class used by notifyAdapter and DateMarkerEvent in Combination
 * <p>
 * posInserted refers to the REAL Event being inserted.
 * posDateMarker refers to DateMarkerEvent (fake or psuedo event only used for graphics in app)
 * been inserted
 * <p>
 * If posDateMarker is -1, it means that no DateMarkerEvent has been added.
 */

public class InsertPositions {
    int posInserted;
    int posDateMarker = -1;

    public InsertPositions(int posInserted, int posDateMarker) {
        this.posInserted = posInserted;
        this.posDateMarker = posDateMarker;
    }

    public boolean isDateMarkerAdded() {
        return posDateMarker > -1;
    }

    public int getPosInserted() {
        return posInserted;
    }

    public int getPosDateMarker() {
        return posDateMarker;
    }
}
