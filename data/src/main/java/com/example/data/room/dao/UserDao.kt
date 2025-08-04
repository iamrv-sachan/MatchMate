package com.example.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.room.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE isMatched = 0 AND isDeclined = 0")
    fun getHomeUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE isMatched = 1 OR isDeclined = 1")
    fun getProfileMatches(): PagingSource<Int, UserEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM user_table WHERE isMatched = 0 AND isDeclined = 0")
    suspend fun getUserCount(): Int
}