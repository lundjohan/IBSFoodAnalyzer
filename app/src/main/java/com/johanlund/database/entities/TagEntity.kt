package com.johanlund.database.entities
/*  TABLE_TAGS + " (  " +
COLUMN_ID + " INTEGER PRIMARY KEY, " +
COLUMN_TAGTYPE + " INTEGER NOT NULL, " +
COLUMN_SIZE + " REAL NOT NULL, " +
COLUMN_EVENT + " INTEGER NOT NULL, " +
" FOREIGN KEY( " + COLUMN_TAGTYPE + ") REFERENCES " + TABLE_TAGTYPES
+ " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
" FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
+ " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
");";*/
data class TagEntity(
        val id: Int,
        val tagType: Int,
        val size: Double,
        val event: Int
)

