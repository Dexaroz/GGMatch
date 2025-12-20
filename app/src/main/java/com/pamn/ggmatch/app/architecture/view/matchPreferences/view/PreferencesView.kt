package com.pamn.ggmatch.app.architecture.view.matchPreferences.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.control.matchPreferences.MatchPreferencesContract
import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.view.matchPreferences.MatchPreferencesTextVariables
import com.pamn.ggmatch.app.architecture.view.matchPreferences.components.matchPreferenceChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun preferencesView(
    uiState: Preferences,
    allRoles: List<LolRole>,
    allLanguages: List<Language>,
    allSchedules: List<PlaySchedule>,
    allPlaystyles: List<Playstyle>,
    presenter: MatchPreferencesContract.Presenter,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A)),
                        ),
                    ),
        )
        Image(
            painter = painterResource(id = R.drawable.twisted),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().blur(40.dp).alpha(0.4f),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.undo),
                    contentDescription = MatchPreferencesTextVariables.BACK_DESCRIPTION,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).clickable { onBack() },
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = MatchPreferencesTextVariables.FILTER_TEAMMATES_TITLE,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = MatchPreferencesTextVariables.FILTER_TEAMMATES_DESCRIPTION,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                preferenceSection(MatchPreferencesTextVariables.LANGUAGE_TITLE) {
                    allLanguages.forEach { lang ->
                        matchPreferenceChip(
                            label = lang.name,
                            selected = lang in uiState.languages,
                            onClick = { presenter.toggleLanguage(lang) },
                        )
                    }
                }

                preferenceSection(MatchPreferencesTextVariables.ROLES_TITLE) {
                    allRoles.forEach { role ->
                        matchPreferenceChip(
                            label = role.name,
                            selected = role in uiState.roles,
                            onClick = { presenter.toggleRole(role) },
                        )
                    }
                }

                preferenceSection(MatchPreferencesTextVariables.SCHEDULE_TITLE) {
                    allSchedules.forEach { schedule ->
                        matchPreferenceChip(
                            label = schedule.name,
                            selected = schedule in uiState.schedules,
                            onClick = { presenter.toggleSchedule(schedule) },
                        )
                    }
                }

                preferenceSection(MatchPreferencesTextVariables.PLAYSTYLE_TITLE) {
                    allPlaystyles.forEach { style ->
                        matchPreferenceChip(
                            label = style.name,
                            selected = style in uiState.playstyles,
                            onClick = { presenter.togglePlaystyle(style) },
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun preferenceSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp),
                )
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                .padding(16.dp),
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            content()
        }
    }
}
