package com.example.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Dob
import com.example.domain.model.Id

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val thumbnailUrl: String,
    val mediumPhotoUrl: String,
    val largePhotoUrl: String,
    val gender: String,
    val location: String,
    val dob: String,
    val isMatched: Boolean = false,
    val isDeclined: Boolean = false,
)