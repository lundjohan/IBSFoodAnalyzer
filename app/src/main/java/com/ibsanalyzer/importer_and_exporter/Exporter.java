package com.ibsanalyzer.importer_and_exporter;

import android.util.Log;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.constants.Constants;
import com.ibsanalyzer.date_time.DateTimeFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Johan on 2018-01-16.
 */

public class Exporter {
    public static void saveToTxt(File filename, List<Event> events) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Event e:events) {
                if (e instanceof Meal) {
                    printMeal(out, (Meal)e);

                }
                else if (e instanceof Other) {
                    printOther(out, (Other)e);
                }
                else if (e instanceof Exercise) {
                    printExercise(out, (Exercise)e);
                }
                else if (e instanceof Bm) {
                    printBm(out, (Bm)e);
                }
                else if (e instanceof Rating) {
                    printRating(out, (Rating)e);
                }
                else {
                    Log.e("Error", "event couldn't be exported to .txt file");
                }
            }
            out.flush();
            out.close();
        }

    }

    private static void printMeal(PrintWriter out, Meal meal) {
        printBeginning(out, "MEAL", meal);
        out.print(meal.getPortions());
        out.print(Constants.DELIMETER);
        //tags
        List<Tag>tags = meal.getTags();
        printTags(out, tags);
        out.println();


    }
    private static void printOther(PrintWriter out, Other other) {
        printBeginning(out, "OTHER", other);
        //tags
        List<Tag>tags = other.getTags();
        printTags(out, tags);
        out.println();
    }
    private static void printExercise(PrintWriter out, Exercise exercise) {
        printBeginning(out, "EXERCISE", exercise);
        //tag
        printTag(out, exercise.getTypeOfExercise());
        out.println();
    }
    private static void printBm(PrintWriter out, Bm bm) {
        printBeginning(out, "BM", bm);
        out.print(bm.getBristol());
        out.print(Constants.DELIMETER);
        out.println(bm.getComplete());
    }
    private static void printRating(PrintWriter out, Rating rating) {
        printBeginning(out, "RATING", rating);
        out.println(rating.getAfter());
    }


    //================================HELP METHODS==============================================
    private static void printBeginning(PrintWriter out, String eventName, Event e) {
        out.print(eventName);
        out.print(Constants.DELIMETER);
        out.print(DateTimeFormat.toSqLiteFormat(e.getTime()));
        out.print(Constants.DELIMETER);
        //cannot now allow linebreak in comments, fucks line up
        out.print(e.getComment().replace(System.getProperty("line.separator"),""));
        out.print(Constants.DELIMETER);
    }
    private static void printTags(PrintWriter out, List<Tag> tags) {
        for (int i=0;i<tags.size();i++) {
            printTag(out, tags.get(i));
            //fulfix för att Other inte ska producera en DELIMETER på sista columnen
            if (i<tags.size()-1) {
                out.print(Constants.DELIMETER);
            }
        }
    }
    private static void printTag(PrintWriter out, Tag t) {
        out.print(t.getTime());
        out.print(Constants.DELIMETER);
        out.print(t.getName());
        out.print(Constants.DELIMETER);
        out.print(t.getSize());
    }

    /**
     could be much more effective by implementing db function that only retrieves Ratings,
     but this can be sloppy doesnt matter. Its not for production use.
     */
    public static void saveTimeAndScoreToTxt(File filename, List<Event> allEvents)throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (Event e : allEvents) {
                if (e instanceof Rating) {
                    printRatingsTimeAndScore(out, (Rating)e);
                    out.println();
                }
            }
            out.flush();
            out.close();
        }
    }

    private static void printRatingsTimeAndScore(PrintWriter out, Rating r) {
        out.print(DateTimeFormat.toSpreadSheetDateTimeFormat(r.getTime()));
        out.print(",");
        out.print(r.getAfter());
    }
}
