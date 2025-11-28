package com.pamn.ggmatch.app.architecture.view.auth.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
import com.pamn.ggmatch.app.architecture.view.auth.RegisterUiTexts
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthFooter
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthHeader
import com.pamn.ggmatch.app.architecture.view.auth.components.GgAuthTitle
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens
import com.pamn.ggmatch.app.architecture.view.shared.components.GgPasswordField
import com.pamn.ggmatch.app.architecture.view.shared.components.GgPrimaryGradientButton
import com.pamn.ggmatch.app.architecture.view.shared.components.GgTextField

@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    uiTexts: RegisterUiTexts = RegisterUiTexts(),
    headerImageRes: Int = R.drawable.register_header,
    logoText: String = "GGMATCH",
    onRegisterClick: (email: String, username: String, password: String) -> Unit,
    onGoToLogin: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = SharedDimens.screenHorizontalPadding),
        ) {
            GgAuthHeader(
                imageRes = headerImageRes,
                logoText = logoText,
            )

            Spacer(modifier = Modifier.height(AuthDimens.titleTopMargin))

            GgAuthTitle(text = uiTexts.title)

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            GgTextField(
                value = email,
                onValueChange = { email = it },
                label = uiTexts.emailPlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            GgTextField(
                value = username,
                onValueChange = { username = it },
                label = uiTexts.usernamePlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            GgPasswordField(
                value = password,
                onValueChange = { password = it },
                label = uiTexts.passwordPlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.buttonTopMargin))

            GgPrimaryGradientButton(
                text = uiTexts.buttonText,
                onClick = { onRegisterClick(email, username, password) },
            )

            GgAuthFooter(
                text = uiTexts.footerText,
                actionText = uiTexts.footerActionText,
                onActionClick = onGoToLogin,
            )
        }
    }
}
