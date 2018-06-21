package com.johanlund.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Tag;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.database.DBHandler;
import com.johanlund.stat_backend.stat_util.ScoreTime;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP;

/**
 * This class encapsulates the logic related to Event, including interactions with MVC model (The
 * model is (currently) parts of database).
 *
 * Should be renamed, handles more than Events
 */
public class EventManager {
    private DBHandler dbHandler;
    private Context c;

    public EventManager(Context context) {
        this.dbHandler = new DBHandler(context);
        this.c = context;
    }

    public boolean eventTypeAtSameTimeAlreadyExists(int type, LocalDateTime ldt) {
        return dbHandler.eventDoesExistOutsideOfEventsTemplate(type, ldt);
    }

    public Event fetchEventById(long id){
        return dbHandler.retrieveEvent(id);
    }
    public boolean tagTemplateDoesntExist(String tagName){
        return dbHandler.getTagTemplateId(tagName) == -1;
    }
    public String retrieveNameOfTagTemplate(long idOfTagTemplate){
        return dbHandler.getTagTemplateName (idOfTagTemplate);
    }
    public List<Tag> getAllTagsWithTime(){
        return dbHandler.getAllTagsWithTime();
    }

    public List<ScoreTime> getCompleteTimes() {
        return dbHandler.getCompleteTimes();
    }

    public List<LocalDateTime> getAllBreaks(){
        List<LocalDateTime> mBreaks = dbHandler.getManualBreaks();
        LocalDateTime lastBreak = dbHandler.getTimeOfLastEvent();
        mBreaks.add(lastBreak);

        //auto breaks
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        int hoursInFrontOfAutoBreak = preferences.getInt("hours_break",
                HOURS_AHEAD_FOR_BREAK_BACKUP);
        List<LocalDateTime> aBreaks = dbHandler.getAutoBreaks(hoursInFrontOfAutoBreak);
        mBreaks.addAll(aBreaks);
        Collections.sort(mBreaks);
        return mBreaks;
    }
}
