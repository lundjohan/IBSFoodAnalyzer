package com.ibsanalyzer.importer;

import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Day;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Johan on 2017-05-29.
 */

public class ImportExcel {

    //this method is added to adjust to Android app
    public static List<Event> toEvents(String fileName) throws Exception {
        return parseToEvents(fileName);
    }

    public static List<Event> parseToEvents(String fileName) throws Exception {
        List<Event> events = new ArrayList<>();
        // Get Sheet
        InputStream inp = new FileInputStream(fileName);

        XSSFWorkbook wb = new XSSFWorkbook(inp);
        Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());

        // rows => days, NB! => LastRow should be INCLUDED (<=)
        outerloop:
        for (int i = 2; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL); //return null if blank
            if (cell == null) {
                continue;
            }
            //Date d = cell.getDateCellValue();
            //LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            //2016-11-12
            String dateStr = cell.getStringCellValue().trim();
            LocalDate date = DateTimeFormat.fromExcelFormat(dateStr);

            // double score = row.getCell(1).getNumericCellValue();

            for (int j = 2; j < row.getLastCellNum(); j++) {
                Cell cell2 = row.getCell(j, Row.RETURN_BLANK_AS_NULL); //return null if blank
                if (cell2 == null) {
                    continue;
                }
                String s = cell2.getStringCellValue();
                if (s != "") {
                    s = s.trim();
                    String s_lower = s.toLowerCase();
                    if (s_lower.startsWith("<ignore>")) {
                        continue outerloop;
                    } else if (s_lower.startsWith("<meal>")) {
                        events.add(strToMeal(date, s));
                    } else if (s_lower.startsWith("<exercise>")) {
                        events.add(strToExercise(date, s));
                    } else if (s_lower.startsWith("<other>")) {
                        events.add(strToOther(date, s));
                    } else if (s_lower.startsWith("<bm>")) {
                        events.add(strToBM(date, s));
                    } else if (s_lower.startsWith("<divider>")) {
                        events.add(strToDivider(date, s));
                    }

                }

            }
        }
        wb.close();
        return events;
    }

    private static Event strToMeal(LocalDate date, String s) {
        LocalDateTime time = strToTime(date, s);
        List<Tag> tags = strToTags(time, s);
        double portions = strToPortions(s);
        return new Meal(time, tags, portions);
    }

    private static Event strToBM(LocalDate date, String s) {
        LocalDateTime time = strToTime(date, s);
        //List<Tag> tags = strToTags(time, s);
        double size = strToSize(s);
        int completeness = strToCompleteness(s);
        int bristol = strToBristol(s);
        return new BM(time,completeness, bristol);
    }

    private static Event strToExercise(LocalDate date, String s) {
        LocalDateTime time = strToTime(date, s);
        List<Tag> tags = strToTags(time, s);
        int intensity = (int) strToIntensity(s);
        return new Exercise(time, tags.get(0), intensity);
    }

    private static Event strToOther(LocalDate date, String s) {
        LocalDateTime time = strToTime(date, s);
        List<Tag> tags = strToTags(time, s);
        return new Other(time, tags);
    }

    private static Event strToDivider(LocalDate date, String s) {
        LocalDateTime time = strToTime(date, s);
        int after = (int) strToAfter(s);
        return new Rating(time, after);
    }

    // arg: '09:15' or '9:15'
    private static LocalDateTime strToTime(LocalDate date, String s) {
        int hour = 12, min = 0;
        Matcher matcher = Constants.TIME_PATTERN.matcher(s);
        if (matcher.find()) {
            String[] hm = matcher.group().split(":");
            hour = Integer.valueOf(hm[0]);
            min = Integer.valueOf(hm[1]);
        }
        LocalTime time = LocalTime.of(hour, min);
        return LocalDateTime.of(date, time);
    }

    private static double strToPortions(String s) {
        double portions = 0;
        Matcher m = Constants.PORTIONS_PATTERN.matcher(s);
        if (m.find()) {
            portions = Double.valueOf(m.group());
        }
        return portions;
    }

    private static double strToIntensity(String s) {
        double intensity = 3;
        Matcher m = Constants.INTENSITY_PATTERN.matcher(s);
        if (m.find()) {
            intensity = Double.valueOf(m.group());
        }
        return intensity;
    }

    private static double strToSize(String s) {
        double size = 3;
        Matcher m = Constants.SIZE_PATTERN.matcher(s);
        if (m.find()) {
            size = Double.valueOf(m.group().replaceFirst(",", "."));
        }
        return size;
    }

    private static int strToCompleteness(String s) {
        Matcher m = Constants.COMPLETENESS_PATTERN.matcher(s);
        int completeness = Integer.MIN_VALUE;
        while (m.find()) {
            completeness = Integer.valueOf(m.group());
        }
        return completeness;
    }

    private static int strToBristol(String s) {
        Matcher m = Constants.BRISTOL_PATTERN.matcher(s);
        int bristol = 4;
        while (m.find()) {
            bristol = Integer.valueOf(m.group());
        }
        return bristol;
    }

    private static double strToAfter(String s) {
        double after = 0;
        Matcher m = Constants.AFTER_PATTERN.matcher(s);
        if (m.find()) {
            after = Double.valueOf(m.group());
        }
        return after;
    }

    // =================TAGS START=======================================
    // arg: "<MEAL><Time>06:00<Tags>[#milk@lacteo*0.3 #halleluja]*2 #yoghurt*2
    // ...#oats<Comment> Great Feeling afterwards."
    // returns: [Tag, Tag, Tag, Tag, Tag]
    private static List<Tag> strToTags(LocalDateTime dateTime, String s) {
        s = s.replace(',', '.');

        // s <- "[#milk@lacteo*0.3 #halleluja]*2 #yoghurt*2 #oats"
        s = trimToTagStrings(s);

        if (s.isEmpty()) {
            return new ArrayList<Tag>();
        }

        // [] <- ["[#milk@lacteo*0.3 #halleluja]*2", "#yoghurt*2", "#oats"] via
        // splitTagStrBundles
        // [] <- ["#milk@lacteo*0.3*0.6", "#halleluja*2", "#yoghurt*2", "#oats"]
        // via getTagStringsOfBrackets
        // ...called by splitTagStrBundles
        List<String> bundlesSplitted = splitTagStrBundles(s); // denna funkar.
        // [] <- ["#milk*0.3*0.6, lacteo*0.3*0.6", "#halleluja*2", "#yoghurt*2",
        // "#oats"]
        List<String> inheritSplitted = splitInheritedTag(bundlesSplitted);

        // [Tag, Tag, Tag, Tag, Tag]
        return removeHashTags(makeTagsFromStrings(dateTime, inheritSplitted));

    }

    // arg: "<MEAL><Time>06:00<Tags>[#milk*0.3 #halleluja]*2 #yoghurt*2
    // ...#oats<Comment> Great Feeling afterwards.";
    // returns: "[#milk*0.3 #halleluja]*2 #yoghurt*2 #oats"
    private static String trimToTagStrings(String s) {
        Matcher matcher = Constants.TAGS_PATTERN.matcher(s);
        if (matcher.find()) {
            s = matcher.group();
        }
        return s.trim();
    }

    // arg: "[#milk@lacteo*0.3 #halleluja]*2 #yoghurt*2 #oats"
    // returns: ["#milk@lacteo*0.6", "#halleluja*2", "#yoghurt*2", "#oats"]
    private static List<String> splitTagStrBundles(String s) {

        List<String> tagStrings = new ArrayList<>();
        // pattern causes split of \s (but not inside [])
        Matcher m = Constants.TAG_GROUP_PATTERN.matcher(s);
        while (m.find()) {

            String s2 = m.group().trim();    //här sker löpning -> l

            if (s2.charAt(0) == '#') {
                tagStrings.add(s2);
            } else if (s2.charAt(0) == '[') {
                tagStrings.addAll(getTagStringsOfBrackets(s2));
            }
        }
        return tagStrings;
    }

    // arg: string of the form '[#milk*3.3 #pizza@wheat*2.2]*2,0'
    // returns array with strings: ['#milk*6.6', '#pizza@wheat*4.4']
    private static List<String> getTagStringsOfBrackets(String s) {
        List<String> multipliedTagStrings = new ArrayList<>();

        // get multiplier after ']*' ex => "2,0"
        Matcher m = Constants.TAG_BRACKET_MULT_PATTERN.matcher(s);
        String multiplier = "";
        while (m.find()) {
            multiplier = m.group();
        }
        s = s.substring(1, s.indexOf(']')).trim(); // [#milk*3.3
        // #pizza@wheat*2.2]*2.0
        // =>#milk*3.3
        // #pizza@wheat*2.2
        String[] tagStrings = s.split("\\s+"); // => #milk*3.3. #pizza@wheat*2.2

        // multiply every tag with multiplier
        for (String tagString : tagStrings) {
            multipliedTagStrings.add(multTag(tagString, multiplier));
        }
        return multipliedTagStrings;
    }

    // arg: ["#milk@lacteo*0.6", "#halleluja*2", "#yoghurt*2", "#oats"]
    // return: [] <- ["#milk*0.6, lacteo*0.6", "#halleluja*2", "#yoghurt*2",
    // "#oats"]
    private static List<String> splitInheritedTag(List<String> strings) {
        List<String> splittedTags = new ArrayList<>();
        for (String s : strings) {
            s = s.trim();
            if (s.contains("@")) {
                splittedTags.addAll(splitInheritedTagHelper(s));
            } else {
                splittedTags.add(s);
            }
        }
        return splittedTags;
    }

    // SO FAR ONLY ONE INHERITENCE ALLOWED FOR EVERY HASHTAG!!!
    // arg: "#cheese@lacteo*0.6" OR #cheese@lacteo ""NOT ALLOWED:
    // #cheese@lacteo@fat*2.0
    // returns: ["cheese*0.6","lacteo*0.6"]
    private static List<String> splitInheritedTagHelper(String s) {
        List<String> multTagStrings = new ArrayList<>();

        // get multiplier after '*' ex => "2.0"
        String mult = "1"; // default
        if (s.contains("*")) {
            String[] strAndMult = s.split("\\*");
            mult = strAndMult[1];
            s = strAndMult[0];
        }
        String[] strsWithoutMult = s.split("@");

        for (String str : strsWithoutMult) {
            multTagStrings.add(multTag(str.trim(), mult));
        }
        return multTagStrings;
    }

    // arg: tagString -> "#pizza@wheat*2.2", mult => "2.0" OR tagString ->
    // "#okra", mult -> "3.2"
    // returns: #pizza@wheat*4.4 OR #okra*3.2
    private static String multTag(String tagString, String mult) {
        // tagString: from #pizza@wheat*2.2 to => ["#pizza@wheat", "2.2"]
        String[] strAndMult = tagString.split("\\*");
        if (strAndMult.length == 1) {
            tagString = tagString + '*' + mult;
        } else if (strAndMult.length == 2) { // ["#pizza@wheat", "2,2"]
            double multiMult = Double.valueOf(strAndMult[1])
                    * Double.valueOf(mult);
            tagString = strAndMult[0] + '*' + Double.toString(multiMult);
        } else {
            System.out.println(
                    "Exception should be thrown for multTag in excel_importer");
        }
        return tagString;
    }

    // arg: ["#milk*0.3, lacteo*0.6", "#halleluja*2", "#yoghurt*2", "#oats"]
    // return: [Tag, Tag, Tag, Tag, Tag]
    private static List<Tag> makeTagsFromStrings(LocalDateTime dateTime, List<String> strings) {
        List<Tag> tags = new ArrayList<>();
        for (String tagStr : strings) {
            String portions = "1";
            String[] tagAndMult = tagStr.split("\\*");
            if (tagAndMult.length == 2) {
                tagStr = tagAndMult[0];
                portions = tagAndMult[1];
            }
            tags.add(new Tag(dateTime, tagStr, Double.valueOf(portions)));
        }
        return tags;
    }

    private static List<Tag> removeHashTags(List<Tag> tags) {
        List<Tag> tagz = new ArrayList<>();
        for (Tag t : tags) {
            if (t.getName().charAt(0) == '#') {
                t.setName(t.getName().substring(1));
            }
            tagz.add(t);
        }
        return tagz;
    }
    // =================TAGS THE END=======================================
}


