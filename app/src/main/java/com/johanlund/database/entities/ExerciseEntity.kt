package com.johanlund.database.entities

class ExerciseEntity(val intensity: Int, id: Int, dateTime: String,
                  comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, comment, hasBreak)