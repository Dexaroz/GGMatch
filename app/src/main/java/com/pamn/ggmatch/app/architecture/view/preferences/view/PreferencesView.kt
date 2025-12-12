package com.pamn.ggmatch.app.architecture.view.preferences.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.preferences.*
import com.pamn.ggmatch.app.architecture.view.preferences.components.preferenceChip
import com.pamn.ggmatch.app.architecture.control.preferences.PreferencesContract

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesView(
    uiState: Preferences,
    allRoles: List<LolRole>,
    allLanguages: List<Language>,
    allSchedules: List<PlaySchedule>,
    allPlaystyles: List<Playstyle>,
    presenter: PreferencesContract.Presenter,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.undo),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() },
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Filter teammates",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Languages
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Language", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allLanguages.forEach { lang ->
                        preferenceChip(
                            label = lang.name,
                            selected = lang in uiState.languages,
                            onClick = { presenter.toggleLanguage(lang) },
                        )
                    }
                }
            }

            // Roles
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Roles", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allRoles.forEach { role ->
                        val isSelected = role in uiState.favoriteRoles
                        preferenceChip(
                            label = role.name,
                            selected = isSelected,
                            enabled = isSelected || uiState.favoriteRoles.size < 2,
                            onClick = { presenter.toggleRole(role) },
                        )
                    }
                }
            }

            // Schedule
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Schedule", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allSchedules.forEach { schedule ->
                        preferenceChip(
                            label = schedule.name,
                            selected = schedule in uiState.playSchedule,
                            onClick = { presenter.toggleSchedule(schedule) },
                        )
                    }
                }
            }

            // Playstyle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Playstyle", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allPlaystyles.forEach { style ->
                        preferenceChip(
                            label = style.name,
                            selected = style in uiState.playstyle,
                            onClick = { presenter.togglePlaystyle(style) },
                        )
                    }
                }
            }
        }
    }
}
