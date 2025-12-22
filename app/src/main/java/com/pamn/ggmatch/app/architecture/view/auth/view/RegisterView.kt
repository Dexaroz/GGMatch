package com.pamn.ggmatch.app.architecture.view.auth.view

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.auth.commands.RegisterUserCommand
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
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

    val tTitle = stringResource(R.string.auth_register_title)
    val tEmail = stringResource(R.string.auth_email_placeholder)
    val tPassword = stringResource(R.string.auth_password_placeholder)
    val tConfirmPassword = stringResource(R.string.auth_register_confirm_password_placeholder)
    val tLoading = stringResource(R.string.auth_register_loading)
    val tButton = stringResource(R.string.auth_register_button)
    val tFooter = stringResource(R.string.auth_register_footer_text)
    val tFooterAction = stringResource(R.string.auth_register_footer_action)

    val tEmptyFields = stringResource(R.string.auth_register_empty_fields_error)
    val tInvalidEmail = stringResource(R.string.auth_register_invalid_email_error)
    val tPasswordTooShort = stringResource(R.string.auth_register_password_too_short_error)
    val tPasswordsDontMatch = stringResource(R.string.auth_register_passwords_dont_match_error)
    val tGenericError = stringResource(R.string.auth_register_generic_error)

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
                ggAuthTitle(text = tTitle)

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = tEmail,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggPasswordField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = tPassword,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggPasswordField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = tConfirmPassword,
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
                    text = if (isLoading) tLoading else tButton,
                    enabled = !isLoading,
                    modifier = Modifier.width(SharedDimens.buttonWidth),
                    onClick = {
                        val trimmedEmail = email.trim()

                        if (trimmedEmail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            errorMessage = tEmptyFields
                            return@ggPrimaryGradientButton
                        }

                        if (!isValidEmail(trimmedEmail)) {
                            errorMessage = tInvalidEmail
                            return@ggPrimaryGradientButton
                        }

                        if (password.length < 8) {
                            errorMessage = tPasswordTooShort
                            return@ggPrimaryGradientButton
                        }

                        if (password != confirmPassword) {
                            errorMessage = tPasswordsDontMatch
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
                                    errorMessage = result.toUserMessage(tGenericError)
                                }
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                ggAuthFooter(
                    text = tFooter,
                    actionText = tFooterAction,
                    onActionClick = onGoToLogin,
                )
            }
        }
    }
}

private fun Result.Error<AppError>.toUserMessage(generic: String): String = generic

private fun isValidEmail(raw: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(raw).matches()
