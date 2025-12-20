package com.pamn.ggmatch.app.architecture.view.matchPreferences.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFF212121))
                .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(32.dp))

        // Top bar
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.undo),
                // Cambio: BACK_DESCRIPTION
                contentDescription = MatchPreferencesTextVariables.BACK_DESCRIPTION,
                tint = Color.White,
                modifier =
                    Modifier
                        .size(28.dp)
                        .clickable { onBack() },
            )

            Spacer(Modifier.width(12.dp))

            Text(
                // Cambio: FILTER_TEAMMATES_TITLE
                text = MatchPreferencesTextVariables.FILTER_TEAMMATES_TITLE,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Languages
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Cambio: LANGUAGE_TITLE
                Text(MatchPreferencesTextVariables.LANGUAGE_TITLE, color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allLanguages.forEach { lang ->
                        matchPreferenceChip(
                            label = lang.name,
                            selected = lang in uiState.languages,
                            onClick = { presenter.toggleLanguage(lang) },
                        )
                    }
                }
            }

            // Roles
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Cambio: ROLES_TITLE
                Text(MatchPreferencesTextVariables.ROLES_TITLE, color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allRoles.forEach { role ->
                        val isSelected = role in uiState.roles
                        matchPreferenceChip(
                            label = role.name,
                            selected = isSelected,
                            onClick = { presenter.toggleRole(role) },
                        )
                    }
                }
            }

            // Schedule
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Cambio: SCHEDULE_TITLE
                Text(MatchPreferencesTextVariables.SCHEDULE_TITLE, color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allSchedules.forEach { schedule ->
                        matchPreferenceChip(
                            label = schedule.name,
                            selected = schedule in uiState.schedules,
                            onClick = { presenter.toggleSchedule(schedule) },
                        )
                    }
                }
            }

            // Playstyle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Cambio: PLAYSTYLE_TITLE
                Text(MatchPreferencesTextVariables.PLAYSTYLE_TITLE, color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allPlaystyles.forEach { style ->
                        matchPreferenceChip(
                            label = style.name,
                            selected = style in uiState.playstyles,
                            onClick = { presenter.togglePlaystyle(style) },
                        )
                    }
                }
            }
        }
    }
}
