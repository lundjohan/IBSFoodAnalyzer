package com.johanlund.database.entities

/*
TABLE_TAGTYPES + " (  " +
COLUMN_ID + " INTEGER PRIMARY KEY," +
COLUMN_TAGNAME + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " +
TYPE_OF + " INTEGER CHECK( " + TYPE_OF + " != " + COLUMN_ID + ")," +
"  " +
" FOREIGN KEY( " + TYPE_OF + ") REFERENCES " + TABLE_TAGTYPES
+ " ( " + COLUMN_ID + ")" + " ON DELETE SET NULL " +
");";*/

data class TagTypeEntity(
        val id: Int,
        val tagName: String,
        val typeOf: Int
)