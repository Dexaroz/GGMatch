package com.pamn.ggmatch.app.architecture.view.auth

data class RegisterTextVariables(
    val title: String = "REGISTER",
    val emailPlaceholder: String = "Email",
    val passwordPlaceholder: String = "Password",
    val confirmPasswordPlaceholder: String = "Confirm password",
    val buttonText: String = "REGISTER",
    val footerText: String = "Already have an account?",
    val footerActionText: String = "Login",
    val emptyFieldsErrorText: String = "Please fill in all fields.",
    val invalidEmailText: String = "Please enter a valid email.",
    val passwordTooShortText: String = "Password must be at least 8 characters.",
    val passwordsDontMatchText: String = "Passwords do not match.",
    val genericErrorText: String = "An error occurred while registering. Please try again.",
)
