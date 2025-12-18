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
    val forgotPasswordText: String = "Forgot your password?",
    val resetEmailSentText: String = "We’ve sent you an email to reset your password.",
    val resetEmailInvalidText: String = "Enter a valid email to reset your password.",
    val googleButtonText: String = "Continue with Google",
)
