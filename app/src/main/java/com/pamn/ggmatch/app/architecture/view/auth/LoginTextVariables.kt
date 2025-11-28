package com.pamn.ggmatch.app.architecture.view.auth

data class LoginTextVariables(
    val title: String = "LOGIN",
    val emailPlaceholder: String = "Email",
    val passwordPlaceholder: String = "Password",
    val buttonText: String = "LOGIN",
    val loadingText: String = "LOADING...",
    val footerText: String = "Don’t have an account?",
    val footerActionText: String = "Register",
    val emptyFieldsErrorText: String = "Email and password are required",
    val invalidEmailErrorText: String = "Please enter a valid email address",
    val genericErrorText: String = "We couldn't log you in. Try again."
)
