package com.ibsanalyzer.importer;

import org.threeten.bp.ZoneId;

import java.util.regex.Pattern;

/**
 * Created by Johan on 2017-05-29.
 */

public interface Constants {
    //=======================REAL FILE===================================
    static final String STOMACH_DIV = "2016-09-10 post_divider.xlsx";
    //====================EXTERNAL TEST FILES (change to test folder)=====
    static final String SAMPLE_TWO_CHUNKS = "sample_two_chunks.xlsx";
    static final String SAMPLE_THREE_DAYS = "sample_3days.xlsx";
    static final String SAMPLE_3DAYS_SIMPLIFIED = "sample_3days_simplified.xlsx";
    static final String SAMPLE_IGNORE = "sample_ignore.xlsx";
    //==================== FOR PRINTS  ==================================
    static final double MIN_QUANTITY_FOR_PRINT = 5;
    static final String NEWLINE = System.getProperty("line.separator");
    //only for prints to out.txt
    //sorting order, only one of them should be set to true.
    //this is occurring in FilePrinter.sortAndFilterTagPoints
    static final boolean SORT_SCORE_FIRST = false;
    static final boolean SORT_BLUEZONES_FIRST = false;
    static final boolean SORT_COMPLETENESS_FIRST = true;
    static final boolean SORT_BRISTOL_FIRST = false;
    //==================== HEURISTIC FLAGS======================================
    static final boolean DO_RELATIONS = true;
    static final boolean DO_JUMP = true;
    static final boolean DO_BM_COMPLETES = true;
    //====================FOR HEURISTIC TOOLS=================================
    //Blue Zones
    static final int BUFFERT_HOURS_BLUEZONES = 24;
    static final double SCORE_ABOVE_ARE_BLUEZONES = 4.8;
    //In future perhaps this is better
    static final int PERCENTS_THAT_ARE_BLUEZONES = 5;

    //Portions
    static final long HOURS_COHERENT_TIME_FOR_PORTIONS = 30;
    static final long ONE_PORTION = 1;
    static final long TWO_PORTIONS = 2;
    static final long THREE_PORTIONS = 3;
    static final long FOUR_PORTIONS = 4;

    //BM
    static final long HOURS_AHEAD_FOR_BM = 30;

    //Jump
    static final int JUMP_HOURS_LIMIT = 2;
    static final double JUMP_MIN_SCORE_DIFF = 0.5;

    static final int HOURS = 24; //in hours
    static final double PLUS_MINUS_QUOTIENT = 1;
    static final double MAX_SCORE = 5;
    static final double MIN_SCORE = 0;
    //=============================REGEX PATTERNS=================================
    static final Pattern TIME_PATTERN = Pattern.compile("(?=.*)\\d{1,2}:\\d{1,2}");
    // Lookbehind: <Tags> possible spaces. Match: all characters. Lookahead: <
    // or position end of line.
    static final Pattern TAGS_PATTERN = Pattern.compile("(?<=(<Tags>\\s{0,10})).*?(?=\\<)|$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE); // |$
    // SIZE_PATTERN: Retrieve the digit after [<Size> and zero to ten spaces]

    // pattern causes split of \s (but not inside [])
    // from function:
    // arg: "[#milk@lacteo*0.3 #halleluja]*2 #yoghurt*2 #oats"
    // returns: ["[#milk@lacteo*0.3 #halleluja]*2", "#yoghurt*2", "#oats"]
    static final Pattern TAG_GROUP_PATTERN = Pattern.compile("\\[.+?\\](\\*\\d+?[.,]?\\d?)" +
                    "?|#[\\p{L}\\p{N}-_@]+(\\*\\d+?[.,]?\\d?)?", //NB. \\p{L} is matching all
            // unicode letters, which \\w is not i Java (the latter only matches ASCII-letters
            // and _ which I also want)
            Pattern.UNICODE_CASE);

    // TAG_MULT_PATTERN [#milk@lacteo*0.3 #halleluja]*2.5 => '2.5'

    // (?<=\]\*) #lookbehind assertion => start looking after ]*
    // \d+? #brackets multiplied with digit/-s
    // (\\.\\d)? # (together) optional dot and digit
    static final Pattern TAG_BRACKET_MULT_PATTERN = Pattern.compile("(?<=\\]\\*)\\d+?(\\.\\d)?",
            Pattern.UNICODE_CASE);

    static final Pattern TAG_NOBRACKET_MULT_PATTERN = Pattern.compile("(?<=\\*)\\d+?(\\.\\d)?",
            Pattern.UNICODE_CASE);

    static final Pattern TAGNAME_PATTERN = Pattern.compile(".*?(?=(\\*)$)"); // |$
    static final Pattern SIZE_PATTERN = Pattern.compile("(?<=<Size>\\s{0,10})\\d([.,]\\d)?",
            Pattern.CASE_INSENSITIVE); // double
    static final Pattern COMPLETENESS_PATTERN = Pattern.compile("(?<=<Completeness>\\s{0,10})" +
            "\\d", Pattern.CASE_INSENSITIVE); // int
    static final Pattern BRISTOL_PATTERN = Pattern.compile("(?<=<Bristol>\\s{0,10})\\d", Pattern
            .CASE_INSENSITIVE); // int
    static final Pattern INTENSITY_PATTERN = Pattern.compile("(?<=<Intensity>\\s{0,10})\\d([.," +
                    "]\\d)?",
            Pattern.CASE_INSENSITIVE); // double
    static final Pattern PORTIONS_PATTERN = Pattern.compile("(?<=<Portions>\\s{0,10})\\d([.,]\\d)?",
            Pattern.CASE_INSENSITIVE); // double
    static final Pattern AFTER_PATTERN = Pattern.compile("(?<=<after>\\s{0,10})\\d([.,]\\d)?",
            Pattern.CASE_INSENSITIVE); // double
    //=================================TIME ZONE===================================
    ZoneId zoneId = ZoneId.systemDefault();
    static final ZoneId ZONE_ID = ZoneId.systemDefault();


}


