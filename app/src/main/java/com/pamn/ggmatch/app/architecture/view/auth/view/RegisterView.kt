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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.Username
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
import kotlinx.coroutines.launch

@Composable
fun registerView(
    modifier: Modifier = Modifier,
    uiTexts: RegisterTextVariables = RegisterTextVariables(),
    headerImageRes: Int = R.drawable.register_header,
    logoText: String = "GGMATCH",
    authRepository: AuthRepository = AppContainer.authRepository,
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var usernameText by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
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
                    value = emailText,
                    onValueChange = {
                        emailText = it
                        errorMessage = null
                    },
                    label = uiTexts.emailPlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                ggTextField(
                    value = usernameText,
                    onValueChange = {
                        usernameText = it
                        errorMessage = null
                    },
                    label = uiTexts.usernamePlaceholder,
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
                    text = if (isLoading) uiTexts.loadingText else uiTexts.buttonText,
                    enabled = !isLoading,
                    modifier =
                        Modifier
                            .width(SharedDimens.buttonWidth),
                    onClick = {
                        if (emailText.isBlank() || usernameText.isBlank() || password.isBlank()) {
                            errorMessage = uiTexts.emptyFieldsErrorText
                            return@ggPrimaryGradientButton
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(emailText.trim()).matches()) {
                            errorMessage = uiTexts.invalidEmailErrorText
                            return@ggPrimaryGradientButton
                        }

                        if (password.length < 6) {
                            errorMessage = uiTexts.weakPasswordErrorText
                            return@ggPrimaryGradientButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null

                            try {
                                val emailValue = Email(emailText.trim())
                                val usernameValue = Username(usernameText.trim())

                                when (
                                    val result =
                                        authRepository.register(
                                            email = emailValue,
                                            password = password,
                                            username = usernameValue,
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
                            } catch (e: IllegalArgumentException) {
                                isLoading = false
                                errorMessage = uiTexts.invalidEmailErrorText
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = uiTexts.genericErrorText
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

private fun Result.Error<AppError>.toUserMessage(
    uiTexts: RegisterTextVariables,
): String = uiTexts.genericErrorText
