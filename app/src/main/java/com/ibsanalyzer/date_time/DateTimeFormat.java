package com.ibsanalyzer.date_time;

import org.threeten.bp.LocalDateTime;

import static android.R.attr.maxLength;

/**
 * Created by Johan on 2017-05-16.
 *
 * Simplistic class but can be useful if decision comes to alter formatting later.
 *
 * See formats acceptable for SQLite here https://www.sqlite.org/datatype3.html
 *
 * One of them are for example YYYY-MM-DDTHH:MM:SS.SSS where the last SSS bundle is arbitrarily long
 * (this fits with LocalDateTime.toString()) => see
 * https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
 */

public class DateTimeFormat {
    //this is the maxlength of
    public static String toSqLiteFormat(LocalDateTime ldt){
        return ldt.toString();
    }
}
