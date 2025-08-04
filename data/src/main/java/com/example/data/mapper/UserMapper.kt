package com.example.data.mapper

import com.example.data.room.UserEntity
import com.example.domain.model.Dob
import com.example.domain.model.Id
import com.example.domain.model.Location
import com.example.domain.model.Name
import com.example.domain.model.Picture
import com.example.domain.model.User

object UserMapper {

    fun User.toEntity(id: String): UserEntity {
        return UserEntity(
            id = id,
            title = this.name.first,
            firstName = this.name.first,
            lastName = this.name.last,
            email = this.email,
            thumbnailUrl = this.picture.thumbnail,
            mediumPhotoUrl = this.picture.medium,
            largePhotoUrl = this.picture.large,
            isMatched = this.isMatched,
            isDeclined = this.isDeclined,
            phone = this.phone,
            gender = this.gender,
            location = this.location.city,
            dob = this.dob.age.toString(),
        )
    }

    fun UserEntity.toDomain(): User {
        val (idName, idValue) = this.id.split("-", limit = 2).let {
            it.getOrNull(0) to it.getOrNull(1)
        }

        return User(
            name = Name(
                title = this.title,
                first = this.firstName,
                last = this.lastName
            ),
            email = this.email,
            phone = this.phone,
            picture = Picture(
                large = this.largePhotoUrl,
                medium = this.mediumPhotoUrl,
                thumbnail = this.thumbnailUrl
            ),
            isMatched = this.isMatched,
            isDeclined = this.isDeclined,
            gender = this.gender,
            location = Location(
                city = this.location,
                country = ""
            ),
            dob = Dob(age = this.dob.toIntOrNull() ?: 0),
            id = Id(
                name = idName ?: this.firstName,
                value = idValue
            )
        )
    }
}