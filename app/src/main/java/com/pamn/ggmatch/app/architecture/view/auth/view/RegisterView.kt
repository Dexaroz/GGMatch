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
import com.pamn.ggmatch.app.architecture.view.auth.RegisterTextVariables
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthFooter
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthHeader
import com.pamn.ggmatch.app.architecture.view.auth.components.ggAuthTitle
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPasswordField
import com.pamn.ggmatch.app.architecture.view.shared.components.ggPrimaryGradientButton
import com.pamn.ggmatch.app.architecture.view.shared.components.ggTextField

@Composable
fun registerView(
    modifier: Modifier = Modifier,
    uiTexts: RegisterTextVariables = RegisterTextVariables(),
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
            ggAuthHeader(
                imageRes = headerImageRes,
                logoText = logoText,
            )

            Spacer(modifier = Modifier.height(AuthDimens.titleTopMargin))

            ggAuthTitle(text = uiTexts.title)

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            ggTextField(
                value = email,
                onValueChange = { email = it },
                label = uiTexts.emailPlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            ggTextField(
                value = username,
                onValueChange = { username = it },
                label = uiTexts.usernamePlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.fieldVerticalSpacing))

            ggPasswordField(
                value = password,
                onValueChange = { password = it },
                label = uiTexts.passwordPlaceholder,
            )

            Spacer(modifier = Modifier.height(AuthDimens.buttonTopMargin))

            ggPrimaryGradientButton(
                text = uiTexts.buttonText,
                onClick = { onRegisterClick(email, username, password) },
            )

            ggAuthFooter(
                text = uiTexts.footerText,
                actionText = uiTexts.footerActionText,
                onActionClick = onGoToLogin,
            )
        }
    }
}
