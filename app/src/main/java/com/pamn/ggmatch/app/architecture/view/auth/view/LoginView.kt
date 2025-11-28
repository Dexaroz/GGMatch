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
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    uiTexts: LoginUiTexts = LoginUiTexts(),
    headerImageRes: Int = R.drawable.login_header,
    logoText: String = "GGMATCH",
    authRepository: AuthRepository = AppContainer.authRepository,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
) {
    var usernameOrEmail by rememberSaveable { mutableStateOf("") }
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
                    },
                    label = uiTexts.usernamePlaceholder,
                )

                Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

                GgPasswordField(
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

                GgPrimaryGradientButton(
                    text = if (isLoading) uiTexts.loadingText else uiTexts.buttonText,
                    enabled = !isLoading,
                    modifier =
                        Modifier
                            .width(SharedDimens.buttonWidth),
                    onClick = {
                        if (usernameOrEmail.isBlank() || password.isBlank()) {
                            errorMessage = uiTexts.emptyFieldsErrorText
                            return@GgPrimaryGradientButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null

                            val email = Email(usernameOrEmail.trim())

                            when (val result = authRepository.login(email, password)) {
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

                Spacer(modifier = Modifier.height(24.dp))

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
    uiTexts.genericErrorText
