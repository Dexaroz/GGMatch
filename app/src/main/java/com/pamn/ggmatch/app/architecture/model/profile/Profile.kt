package com.pamn.ggmatch.app.architecture.model.profile

data class Profile(
    val id: Int,
    val name: String,
    val nickname: String,
    val age: Int,
    val description: String,
    val imageRes: Int,
    val backgroundImageRes: Int,
)
