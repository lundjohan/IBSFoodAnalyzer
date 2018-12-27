package com.johanlund.database.entities

class ExerciseEntity(val intensity: Int, id: Int, dateTime: String,
                 typeOfEvent: Int, comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, typeOfEvent, comment, hasBreak)