package com.johanlund.dao

import com.johanlund.base_classes.Event
import com.johanlund.base_classes.Tag
import com.johanlund.database.entities.*
import com.johanlund.model.TagType
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

    fun tagTypeExists(tagTypeName: String):Boolean

    //only insert TagTypes that don't already exist (same name)
    fun insertTagTypesFromExternalDatabase(pathToExternal: String)

    fun insertEventTemplatesFromExternalDatabase(pathToExternal: String) {
    }


    fun getTagTypesEntities(): List<TagTypeEntity>
    fun getTagsEntities(): List<TagEntity>
    fun getMealsEntities(): List<MealEntity>
    fun getOthersEntities(): List<OtherEntity>
    fun getExercisesEntities(): List<ExerciseEntity>
    fun getBmsEntities(): List<BmEntity>
    fun getRatingsEntities(): List<RatingEntity>
}