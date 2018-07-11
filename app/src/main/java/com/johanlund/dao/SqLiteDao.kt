package com.johanlund.dao

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.johanlund.base_classes.Event
import com.johanlund.base_classes.Tag
import com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP
import com.johanlund.database.DBHandler
import com.johanlund.stat_backend.stat_util.ScoreTime
import org.threeten.bp.LocalDateTime
import java.io.File
import java.util.*

class SqLiteDao(val c: Context): Dao{
    val dbHandler: DBHandler = DBHandler(c)
    override fun eventTypeAtSameTimeAlreadyExists(type: Int, ldt: LocalDateTime): Boolean {
        return dbHandler.eventDoesExistOutsideOfEventsTemplate(type, ldt)
    }

    override fun fetchEventById(id: Long): Event {
        return dbHandler.retrieveEvent(id)
    }

    override fun tagTemplateDoesntExist(tagName: String): Boolean {
        return dbHandler.getTagTemplateId(tagName) == -1L
    }

    override fun retrieveNameOfTagTemplate(idOfTagTemplate: Long): String {
        return dbHandler.getTagTemplateName(idOfTagTemplate)
    }

    override fun getAllTagsWithTime(): List<Tag> {
        return dbHandler.allTagsWithTime
    }

    override fun getCompleteTimes(): List<ScoreTime> {
        return dbHandler.completeTimes
    }

    override fun getAllBreaks(): List<LocalDateTime> {
        val mBreaks = dbHandler.manualBreaks
        val lastBreak = dbHandler.timeOfLastEvent
        mBreaks.add(lastBreak)

        //auto breaks
        val preferences = PreferenceManager.getDefaultSharedPreferences(c)
        val hoursInFrontOfAutoBreak = preferences.getInt("hours_break",
                HOURS_AHEAD_FOR_BREAK_BACKUP)
        val aBreaks = dbHandler.getAutoBreaks(hoursInFrontOfAutoBreak)
        mBreaks.addAll(aBreaks)
        Collections.sort(mBreaks)
        return mBreaks
    }

    override fun insertTagTypesFromExternalDatabase(pathToExternal: File) {
        dbHandler.insertTagTypesFromExternalDatabase(pathToExternal)
    }
}