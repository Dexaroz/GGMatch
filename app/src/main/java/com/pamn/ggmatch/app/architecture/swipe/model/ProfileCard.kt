package com.pamn.ggmatch.app.architecture.swipe.model

data class ProfileCard(
    val name: String,
    val age: Int,
    val description: String,
)

fun getTestCards() =
    listOf(
        ProfileCard("Laura", 28, "Apasionada por el diseño y el arte moderno. Su pasión es el código Kotlin y Compose."),
        ProfileCard("Carlos", 32, "Ingeniero de software y amante del senderismo. Siempre buscando la mejor solución arquitectónica."),
        ProfileCard("María", 25, "Estudiante de música y adicta al café. Le gusta la composición algorítmica y los patrones de diseño."),
    )