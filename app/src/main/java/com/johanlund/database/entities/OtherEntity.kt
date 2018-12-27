package com.johanlund.database.entities

class OtherEntity(id: Int, dateTime: String,
                 comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, comment, hasBreak)