package com.ibsanalyzer.importer_and_exporter;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.DELIMETER;
import static com.ibsanalyzer.diary.R.id.portions;

/**
 * Created by Johan on 2017-06-14.
 */

public class Importer {
    final static String meal = "MEAL", other = "OTHER", exercise = "EXERCISE", bm = "BM", rating
            = "RATING";
    final static int DATE_LENGTH = "2017-04-27T06:30".length();
    final static int HAS_BREAK_LENGTH = "f".length(); //or "t"
    public static Event lineToEvent(String line) {

        Event event = null;

        if (line.startsWith(meal)) {
            event = lineToMeal(line.substring(meal.length() + DELIMETER.length()));
        } else if (line.startsWith(other)) {
            event = lineToOther(line.substring(other.length() + DELIMETER.length()));
        } else if (line.startsWith(exercise)) {
            event = lineToExercise(line.substring(exercise.length() + DELIMETER.length()));
        } else if (line.startsWith(bm)) {
            event = lineToBm(line.substring(bm.length() + DELIMETER.length()));
        } else if (line.startsWith(rating)) {
            event = lineToRating(line.substring(rating.length() + DELIMETER.length()));
        }
        return event;
    }


    //notice that portions is before tags
    //=> 2017-04-28T10:50|This is a comment|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10
    // :50|vit_fisk|0.7

    static Meal lineToMeal(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        String comment = lineToComment(line);
        line = line.substring(comment.length() + DELIMETER.length());
        boolean hasBreak = lineToBreak(line);
        line = line.substring(HAS_BREAK_LENGTH + DELIMETER.length());

        //portions
        double portions = lineToPortions(line);
        line = line.substring(line.indexOf(DELIMETER) + 1);

        //tags
        List<Tag> tags = lineToTags(line);
        return new Meal(ldt, comment, hasBreak, tags, portions);
    }

    static Other lineToOther(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        String comment = lineToComment(line);
        line = line.substring(comment.length() + DELIMETER.length());
        boolean hasBreak = lineToBreak(line);
        line = line.substring(HAS_BREAK_LENGTH + DELIMETER.length());
        List<Tag> tags = lineToTags(line);
        return new Other(ldt,comment, hasBreak, tags);
    }

    //2017-04-27T18:30|This is a comment|2017-04-27T18:30|springer|1.0
    static Exercise lineToExercise(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        String comment = lineToComment(line);
        line = line.substring(comment.length() + DELIMETER.length());
        boolean hasBreak = lineToBreak(line);
        line = line.substring(HAS_BREAK_LENGTH + DELIMETER.length());
        Tag t = lineToTag(line);
        int lastDel = line.lastIndexOf(DELIMETER);
        double intensity = lineToIntensity(line.substring(lastDel + 1));
        return new Exercise(ldt, comment, hasBreak, t, (int) intensity);
    }

    //2017-04-27T17:00|This is a comment|7|3
    static Bm lineToBm(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        String comment = lineToComment(line);
        line = line.substring(comment.length() + DELIMETER.length());
        boolean hasBreak = lineToBreak(line);
        line = line.substring(HAS_BREAK_LENGTH + DELIMETER.length());
        //not very safe but should work, in production below must be made safer of course
        //7|3
        int bristol = Integer.parseInt(line.substring(0, 1));
        int completeness = Integer.parseInt(line.substring(2));
        return new Bm(ldt, comment, hasBreak, completeness, bristol);
    }


    //2017-05-01T17:00|This is a comment|5
    static Rating lineToRating(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        String comment = lineToComment(line);
        line = line.substring(comment.length() + DELIMETER.length());
        boolean hasBreak = lineToBreak(line);
        line = line.substring(HAS_BREAK_LENGTH + DELIMETER.length());
        //not very safe but should work, in production below must be made safer of course
        //7|3
        int indDel = line.indexOf(DELIMETER);
        int after = Integer.parseInt(line.substring(indDel + 1));
        return new Rating(ldt,comment, hasBreak, after);
    }


    //2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
    private static List<Tag> lineToTags(String line) {
        List<Tag> tags = new ArrayList<>();
        do {
            line = lineToTag(line, tags);
        } while (line.length() > 0);
        return tags;
    }


    //2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
    //
    private static String lineToTag(String line, List<Tag> tags) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        int tagNameEnd = line.indexOf(DELIMETER);
        String tagName = line.substring(0, tagNameEnd);
        int tagSizeEnd = line.indexOf(DELIMETER, tagNameEnd + 1);
        int endOfFile = -1;
        double tagSize = tagSizeEnd == endOfFile ? Double.parseDouble(line.substring(tagNameEnd +
                1)) :
                Double.parseDouble(line.substring(tagNameEnd + 1, tagSizeEnd));
        tags.add(new Tag(ldt, tagName, tagSize));
        return tagSizeEnd == endOfFile ? "" : line.substring(tagSizeEnd + 1);
    }

    private static Tag lineToTag(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());
        int tagNameEnd = line.indexOf(DELIMETER);
        String tagName = line.substring(0, tagNameEnd);
        int tagSizeEnd = line.indexOf(DELIMETER, tagNameEnd + 1);
        int endOfFile = -1;
        double tagSize = tagSizeEnd == endOfFile ? Double.parseDouble(line.substring(tagNameEnd +
                1)) :
                Double.parseDouble(line.substring(tagNameEnd + 1, tagSizeEnd));
        return new Tag(ldt, tagName, tagSize);
    }


    //=> 0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
    private static double lineToPortions(String line) {
        int portionEndInd = line.indexOf(DELIMETER);
        return Double.parseDouble(line.substring(0, portionEndInd));
    }

    private static double lineToIntensity(String line) {
        int portionEndInd = line.indexOf(DELIMETER);
        return Double.parseDouble(line);
    }

    //=> starting with date like 2017-04-27T06:30
    private static LocalDateTime lineToTime(String line) {
        return DateTimeFormat.fromSqLiteFormat(line.substring(0, DATE_LENGTH));
    }
    private static boolean lineToBreak(String line){
        return line.charAt(0) == 't';
    }
    //=> This is a comment|2017-04-28T...
    private static String lineToComment(String line) {
        int commentEndInd = line.indexOf(DELIMETER);
        return line.substring(0, commentEndInd);
    }
}
