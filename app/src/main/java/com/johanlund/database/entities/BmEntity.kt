package com.johanlund.database.entities

class BmEntity(val complete: Int, val bristol: Int, id: Int, dateTime: String,
                 typeOfEvent: Int, comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, typeOfEvent, comment, hasBreak)