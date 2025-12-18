package com.pamn.ggmatch.app.architecture.view.auth.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginUserCommand
import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginWithGoogleCommand
import com.pamn.ggmatch.app.architecture.control.auth.commands.SendPasswordResetEmailCommand
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
import com.pamn.ggmatch.app.architecture.view.auth.LoginUiTexts
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthFooter
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthHeader
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthTitle
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens
import com.pamn.ggmatch.app.architecture.view.shared.components.GgPasswordField
import com.pamn.ggmatch.app.architecture.view.shared.components.GgPrimaryGradientButton
import com.pamn.ggmatch.app.architecture.view.shared.components.GgTextField
import com.pamn.ggmatch.app.controllers.AuthCommandHandlers
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    uiTexts: LoginUiTexts = LoginUiTexts(),
    headerImageRes: Int = R.drawable.login_header,
    logoText: String = "GGMATCH",
    authHandlers: AuthCommandHandlers = AppContainer.authCommandHandlers,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    onRequestGoogleIdToken: suspend () -> String?,
) {
    var usernameOrEmail by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var infoMessage by rememberSaveable { mutableStateOf<String?>(null) }

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
            GgAuthHeader(
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
                GgAuthTitle(text = uiTexts.title)

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                GgTextField(
                    value = usernameOrEmail,
                    onValueChange = {
                        usernameOrEmail = it
                        errorMessage = null
                        infoMessage = null
                    },
                    label = uiTexts.usernamePlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                GgPasswordField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                        infoMessage = null
                    },
                    label = uiTexts.passwordPlaceholder,
                )

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        val email = runCatching { Email(usernameOrEmail.trim()) }.getOrNull()
                        if (email == null) {
                            errorMessage = uiTexts.resetEmailInvalidText
                            infoMessage = null
                            return@TextButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            infoMessage = null

                            val command = SendPasswordResetEmailCommand(email)
                            when (val result = authHandlers.resetPassword(command)) {
                                is Result.Ok -> {
                                    isLoading = false
                                    infoMessage = uiTexts.resetEmailSentText
                                }

                                is Result.Error -> {
                                    isLoading = false
                                    errorMessage = result.toUserMessage(uiTexts)
                                }
                            }
                        }
                    },
                ) {
                    Text(
                        text = uiTexts.forgotPasswordText,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.Start),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                if (infoMessage != null) {
                    Text(
                        text = infoMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.Start),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Spacer(modifier = Modifier.height(AuthDimens.buttonTopMargin))

                GgPrimaryGradientButton(
                    text = if (isLoading) uiTexts.loadingText else uiTexts.buttonText,
                    enabled = !isLoading,
                    modifier =
                        Modifier
                            .width(SharedDimens.buttonWidth),
                    onClick = {
                        if (usernameOrEmail.isBlank() || password.isBlank()) {
                            errorMessage = uiTexts.emptyFieldsErrorText
                            infoMessage = null
                            return@GgPrimaryGradientButton
                        }

                        val email = runCatching { Email(usernameOrEmail.trim()) }.getOrNull()
                        if (email == null) {
                            errorMessage = uiTexts.resetEmailInvalidText
                            infoMessage = null
                            return@GgPrimaryGradientButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            infoMessage = null

                            val command =
                                LoginUserCommand(
                                    email = email.value,
                                    password = password,
                                )

                            when (val result = authHandlers.loginUser(command)) {
                                is Result.Ok -> {
                                    isLoading = false
                                    onLoginSuccess()
                                }

                                is Result.Error -> {
                                    isLoading = false
                                    errorMessage = result.toUserMessage(uiTexts)
                                }
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedButton(
                    enabled = !isLoading,
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            infoMessage = null

                            val idToken = onRequestGoogleIdToken()
                            if (idToken.isNullOrBlank()) {
                                isLoading = false
                                errorMessage = uiTexts.genericErrorText
                                return@launch
                            }

                            val command = LoginWithGoogleCommand(idToken = idToken)

                            when (val result = authHandlers.loginWithGoogle(command)) {
                                is Result.Ok -> {
                                    isLoading = false
                                    onLoginSuccess()
                                }

                                is Result.Error -> {
                                    isLoading = false
                                    errorMessage = result.toUserMessage(uiTexts)
                                }
                            }
                        }
                    },
                ) {
                    Text(text = uiTexts.googleButtonText)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GgAuthFooter(
                    text = uiTexts.footerText,
                    actionText = uiTexts.footerActionText,
                    onActionClick = onGoToRegister,
                )
            }
        }
    }
}

private fun Result.Error<AppError>.toUserMessage(uiTexts: LoginUiTexts): String =
    when (val appError = error) {
        is AppError.Unexpected ->
            appError.message
                ?.takeIf { it.isNotBlank() }
                ?: uiTexts.genericErrorText

        else -> uiTexts.genericErrorText
    }
