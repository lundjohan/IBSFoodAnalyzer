package com.ibsanalyzer.importer;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.DELIMETER;
import static com.ibsanalyzer.inputday.R.id.portions;

/**
 * Created by Johan on 2017-06-14.
 */

public class Importer {
    final static String meal = "MEAL", other = "OTHER", exercise = "EXERCISE", bm = "BM", rating
            = "RATING";
    final static int DATE_LENGTH = "2017-04-27T06:30".length();

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

    private static Event lineToExercise(String substring) {
        return null;
    }

    private static Event lineToBm(String substring) {
        return null;
    }

    private static Event lineToRating(String substring) {
        return null;
    }

    //notice that portions is before tags
    //=> 2017-04-28T10:50|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10
    // :50|vit_fisk|0.7

    static Meal lineToMeal(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());

        //=> 0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
        //portions
        double portions = lineToPortions(line);
        line = line.substring(line.indexOf(DELIMETER) + 1);
        //tags
        List<Tag> tags = lineToTags(line);
        return new Meal(ldt, tags, portions);
    }

    private static Event lineToOther(String line) {
        LocalDateTime ldt = lineToTime(line);
        line = line.substring(DATE_LENGTH + DELIMETER.length());

        //=> 2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
        //tags
        List<Tag> tags = lineToTags(line);
        return new Other(ldt, tags);
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

    //=> 0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0|2017-04-28T10:50|vit_fisk|0.7
    private static double lineToPortions(String line) {
        int portionEndInd = line.indexOf(DELIMETER);
        return Double.parseDouble(line.substring(0, portionEndInd));
    }

    //=> starting with date like 2017-04-27T06:30
    private static LocalDateTime lineToTime(String line) {
        return DateTimeFormat.fromSqLiteFormat(line.substring(0, DATE_LENGTH));
    }

}
