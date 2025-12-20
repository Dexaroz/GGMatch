package com.pamn.ggmatch.app.architecture.view.auth.view

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.auth.commands.RegisterUserCommand
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
import com.pamn.ggmatch.app.architecture.view.auth.RegisterTextVariables
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthFooter
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthHeader
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthTitle
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPasswordField
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPrimaryGradientButton
import com.pamn.ggmatch.app.architecture.view.shared.components.ggTextField
import com.pamn.ggmatch.app.controllers.AuthController
import kotlinx.coroutines.launch

@Composable
fun registerView(
    modifier: Modifier = Modifier,
    uiTexts: RegisterTextVariables = RegisterTextVariables(),
    headerImageRes: Int = R.drawable.register_header,
    logoText: String = "GGMATCH",
    authController: AuthController = AppContainer.authController,
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
        ) {
            ggAuthHeader(
                imageRes = headerImageRes,
                logoText = logoText,
            )

            Spacer(modifier = Modifier.height(AuthDimens.titleTopMargin))

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SharedDimens.screenHorizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ggAuthTitle(text = uiTexts.title)

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = uiTexts.emailPlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggPasswordField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = uiTexts.passwordPlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggPasswordField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = uiTexts.confirmPasswordPlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.Start),
                    )
                    Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))
                }

                Spacer(modifier = Modifier.height(AuthDimens.buttonTopMargin))

                ggPrimaryGradientButton(
                    text = if (isLoading) uiTexts.buttonText else uiTexts.buttonText,
                    enabled = !isLoading,
                    modifier = Modifier.width(SharedDimens.buttonWidth),
                    onClick = {
                        val trimmedEmail = email.trim()

                        if (trimmedEmail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            errorMessage = uiTexts.emptyFieldsErrorText
                            return@ggPrimaryGradientButton
                        }

                        if (!isValidEmail(trimmedEmail)) {
                            errorMessage = uiTexts.invalidEmailText
                            return@ggPrimaryGradientButton
                        }

                        if (password.length < 8) {
                            errorMessage = uiTexts.passwordTooShortText
                            return@ggPrimaryGradientButton
                        }

                        if (password != confirmPassword) {
                            errorMessage = uiTexts.passwordsDontMatchText
                            return@ggPrimaryGradientButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null

                            when (
                                val result =
                                    authController.registerUser(
                                        RegisterUserCommand(
                                            email = trimmedEmail,
                                            password = password,
                                        ),
                                    )
                            ) {
                                is Result.Ok -> {
                                    isLoading = false
                                    onRegisterSuccess()
                                }
                                is Result.Error -> {
                                    isLoading = false
                                    errorMessage = result.toUserMessage(uiTexts)
                                }
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                ggAuthFooter(
                    text = uiTexts.footerText,
                    actionText = uiTexts.footerActionText,
                    onActionClick = onGoToLogin,
                )
            }
        }
    }
}

private fun Result.Error<AppError>.toUserMessage(uiTexts: RegisterTextVariables): String = uiTexts.genericErrorText

private fun isValidEmail(raw: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(raw).matches()
