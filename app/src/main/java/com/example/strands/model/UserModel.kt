package com.example.strands.model

data class UserModel(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val bio: String? = "",
    val imageUrl: String = "",
    val uid: String = ""
)
