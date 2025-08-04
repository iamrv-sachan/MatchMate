package com.example.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.room.dao.UserDao

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class MatchMateDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}