package com.pamn.ggmatch.app.architecture.view.auth

data class LoginUiTexts(
    val title: String = "LOGIN",
    val usernamePlaceholder: String = "Username",
    val passwordPlaceholder: String = "Password",
    val buttonText: String = "LOGIN",
    val loadingText: String = "LOADING...",
    val footerText: String = "Don’t have an account?",
    val footerActionText: String = "REGISTER",
    val emptyFieldsErrorText: String = "Please fill in all fields",
    val genericErrorText: String = "Something went wrong. Please try again.",
)
