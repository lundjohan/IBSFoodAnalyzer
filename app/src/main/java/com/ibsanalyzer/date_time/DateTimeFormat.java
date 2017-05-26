package com.ibsanalyzer.date_time;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;


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
    public static LocalDateTime fromSqLiteFormat(String str){
        //ugly format fix
        if (str.length()== 16) {
            str = str + ":00";
        }
      //  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }
}
