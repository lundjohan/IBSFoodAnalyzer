package com.ibsanalyzer.date_time;

import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
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
    //MAY 26 2017
   /* public static LocalDate fromTextViewDateFormat(String str){
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy");
        Log.d("Debug", formatter.toString());
        return LocalDate.parse(str, formatter);
    }*/

    public static LocalDate fromTextViewDateFormat(String str){
        return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
    }
    public static LocalTime fromTextViewTimeFormat(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(str, formatter);
    }

    //TODO
/*    public static String toTextViewFormat(LocalDate ld){
        return String.valueOf(ld.getMonth()  + " " +String.format("%02d", ld.getDayOfMonth())+" "+ ld.getYear());  //return ld.getMonth().toString()+" " + String.valueOf(ld.getDayOfMonth()) + ", " + String.valueOf(ld.getYear());
    }*/
    public static String toTextViewFormat(LocalDate ld){
        return String.valueOf(ld.getYear() + "-" + String.format("%02d", ld.getMonthValue())+"-" + String.format("%02d", ld.getDayOfMonth()));  //return ld.getMonth().toString()+" " + String.valueOf(ld.getDayOfMonth()) + ", " + String.valueOf(ld.getYear());
    }
    public static String toTextViewFormat(LocalTime lt){
        return String.format("%02d", lt.getHour()) + ":" + String.format("%02d", lt.getMinute());
    }
}
