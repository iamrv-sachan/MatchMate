package com.example.domain.model

data class UserResponse(
    val results: List<User>
)

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val dob: Dob,
    val phone: String,
    val id: Id,
    val picture: Picture,
    val isMatched: Boolean = false,
    val isDeclined: Boolean = false
)

data class Name(
    val title: String,
    val first: String,
    val last: String
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class Location(
    val city: String,
    val country: String
)

data class Dob(
    val age: Int
)

data class Id(
    val name: String,
    val value: String?
)