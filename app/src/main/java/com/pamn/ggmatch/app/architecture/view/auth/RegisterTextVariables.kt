package com.pamn.ggmatch.app.architecture.view.auth

data class RegisterTextVariables(
    val title: String = "REGISTER",
    val emailPlaceholder: String = "Email",
    val usernamePlaceholder: String = "Username",
    val passwordPlaceholder: String = "Password",
    val buttonText: String = "REGISTER",
    val footerText: String = "Already have an account?",
    val footerActionText: String = "Login",
    val loadingText: String = "CREATING ACCOUNT...",
    val emptyFieldsErrorText: String = "All fields are required",
    val invalidEmailErrorText: String = "Please enter a valid email address",
    val weakPasswordErrorText: String = "Password must be at least 6 characters",
    val genericErrorText: String = "We couldn't create your account. Try again."
)
