package com.pamn.ggmatch.app.architecture.view.auth

data class RegisterTextVariables(
    val title: String = "REGISTER",
    val emailPlaceholder: String = "Email",
    val usernamePlaceholder: String = "Username",
    val passwordPlaceholder: String = "Password",
    val buttonText: String = "REGISTER",
    val loadingText: String = "REGISTERING...",
    val footerText: String = "Already have an account?",
    val footerActionText: String = "LOGIN",
    val emptyFieldsErrorText: String = "Please fill in all fields",
    val genericErrorText: String = "Something went wrong. Please try again.",
)
