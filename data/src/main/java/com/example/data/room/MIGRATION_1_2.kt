package com.example.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user_table ADD COLUMN mediumPhotoUrl TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE user_table ADD COLUMN largePhotoUrl TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user_table ADD COLUMN gender TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE user_table ADD COLUMN location TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE user_table ADD COLUMN dob TEXT NOT NULL DEFAULT ''")
    }
}