package com.johanlund.database.entities

class RatingEntity(val after: Int, id: Int, dateTime: String,
             comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime,  comment, hasBreak)