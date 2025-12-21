package com.pamn.ggmatch.app.architecture.view.auth.view

import android.app.Activity
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginUserCommand
import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginWithGoogleCommand
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
import com.pamn.ggmatch.app.architecture.view.auth.LoginTextVariables
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthFooter
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthHeader
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthTitle
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPasswordField
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPrimaryGradientButton
import com.pamn.ggmatch.app.architecture.view.shared.components.ggTextField
import kotlinx.coroutines.launch

@Composable
fun loginView(
    modifier: Modifier = Modifier,
    uiTexts: LoginTextVariables = LoginTextVariables(),
    headerImageRes: Int = R.drawable.login_header,
    logoText: String = "GGMATCH",
    authController: com.pamn.ggmatch.app.controllers.AuthController = AppContainer.authController,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val webClientId = context.getString(R.string.default_web_client_id)

    val gso =
        remember(webClientId) {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(webClientId)
                .build()
        }

    val googleClient = remember(gso) { GoogleSignIn.getClient(context, gso) }

    val googleLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken.isNullOrBlank()) {
                    errorMessage = uiTexts.genericErrorText
                    return@rememberLauncherForActivityResult
                }

                scope.launch {
                    isLoading = true
                    errorMessage = null

                    when (val res = authController.loginWithGoogle(LoginWithGoogleCommand(idToken))) {
                        is Result.Ok -> {
                            isLoading = false
                            onLoginSuccess()
                        }
                        is Result.Error -> {
                            isLoading = false
                            errorMessage = uiTexts.genericErrorText
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = uiTexts.genericErrorText
            }
        }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
        ) {
            ggAuthHeader(
                imageRes = headerImageRes,
                logoText = logoText,
            )

            Spacer(modifier = Modifier.height(AuthDimens.titleTopMargin))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = SharedDimens.screenHorizontalPadding),
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
                    modifier = Modifier.width(SharedDimens.buttonWidth),
                    onClick = {
                        if (emailText.isBlank() || password.isBlank()) {
                            errorMessage = uiTexts.emptyFieldsErrorText
                            return@ggPrimaryGradientButton
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(emailText.trim()).matches()) {
                            errorMessage = uiTexts.invalidEmailErrorText
                            return@ggPrimaryGradientButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null

                            val cmd = LoginUserCommand(email = emailText.trim(), password = password)

                            when (val result = authController.loginUser(cmd)) {
                                is Result.Ok -> {
                                    isLoading = false
                                    onLoginSuccess()
                                }
                                is Result.Error -> {
                                    isLoading = false
                                    errorMessage = uiTexts.genericErrorText
                                }
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        errorMessage = null
                        googleLauncher.launch(googleClient.signInIntent)
                    },
                    enabled = !isLoading,
                    modifier = Modifier.width(210.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(25.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(uiTexts.googleButtonText)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ggAuthFooter(
                    text = uiTexts.footerText,
                    actionText = uiTexts.footerActionText,
                    onActionClick = onGoToRegister,
                )
            }
        }
    }
}
