package com.johanlund.database.entities

class RatingEntity(val after: Int, id: Int, dateTime: String,
                 typeOfEvent: Int, comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, typeOfEvent, comment, hasBreak)