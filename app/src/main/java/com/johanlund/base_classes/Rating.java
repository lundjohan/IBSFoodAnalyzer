package com.johanlund.base_classes;

import com.johanlund.constants.Constants;

import org.threeten.bp.LocalDateTime;

;

//this class is an exception from EventModel rule: it does not use Tags.
public class Rating extends Event {
    //after should be between 1 and 7
    private int after;
    public Rating(LocalDateTime ldt, int after) {
        super(ldt);
        this.after = after;
    }
    public Rating(LocalDateTime time, String comment, int after) {
        super(time, comment);
        this.after = after;
    }

    public Rating(LocalDateTime ldt, String comment, boolean hasBreak, int after) {
        super(ldt, comment, hasBreak);
        this.after = after;
    }


    public static String pointsToText(int score) {
        String text = "OUT OF RANGE";
        switch (score) {
            case 1:
                text = "Awful";
                break;
            case 2:
                text = "Bad";
                break;
            case 3:
                text = "Deficient";
                break;
            case 4:
                text = "OK";
                break;
            case 5:
                text = "Great";
                break;
            case 6:
                text = "Phenomenal";
                break;
        }
        return text;
    }

    public int getAfter() {
        return after;
    }

    @Override
    public int getType() {
        return Constants.RATING;
    }
}
