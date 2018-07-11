package com.johanlund.dao

import com.johanlund.base_classes.Event
import com.johanlund.base_classes.Tag
import com.johanlund.stat_backend.stat_util.ScoreTime
import org.threeten.bp.LocalDateTime
import java.io.File

interface Dao {
    fun eventTypeAtSameTimeAlreadyExists(type: Int, ldt: LocalDateTime): Boolean
    fun fetchEventById(id: Long): Event
    fun retrieveNameOfTagTemplate(idOfTagTemplate: Long): String
    fun tagTemplateDoesntExist(tagName: String): Boolean
    fun getAllTagsWithTime(): List<Tag>
    fun getCompleteTimes(): List<ScoreTime>
    fun getAllBreaks(): List<LocalDateTime>

    //only insert TagTypes that don't already exist (same name)
    fun insertTagTypesFromExternalDatabase(pathToExternal: File)
}