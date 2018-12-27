package com.johanlund.database.entities

class BmEntity(val complete: Int, val bristol: Int, id: Int, dateTime: String,
                comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, comment, hasBreak)