package com.pamn.ggmatch.app.architecture.view.preferences.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.view.preferences.components.preferenceChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun preferencesScreen(
    initial: Preferences,
    allRoles: List<LolRole>,
    allLanguages: List<Language>,
    allSchedules: List<PlaySchedule>,
    allPlaystyles: List<Playstyle>,
    onSave: (Preferences) -> Unit,
    onBack: () -> Unit,
) {
    var selectedRoles by remember { mutableStateOf(initial.favoriteRoles.toMutableSet()) }
    var selectedLanguages by remember { mutableStateOf(initial.languages.toMutableSet()) }
    var selectedSchedule by remember { mutableStateOf(initial.playSchedule.toMutableSet()) }
    var selectedPlaystyle by remember { mutableStateOf(initial.playstyle.toMutableSet()) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFF212121))
                .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(32.dp))

        // ---------------- TOP BAR ----------------
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.undo),
                contentDescription = "Volver",
                tint = Color.White,
                modifier =
                    Modifier
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

        // ---------------- CENTER CONTENT ----------------

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp, bottom = 32.dp),
            // Añadido padding bottom para scroll final
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ---------------- LANGUAGE ----------------
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
                            selected = lang in selectedLanguages,
                            onClick = {
                                selectedLanguages =
                                    if (lang in selectedLanguages) {
                                        (selectedLanguages - lang).toMutableSet()
                                    } else {
                                        (selectedLanguages + lang).toMutableSet()
                                    }
                            },
                        )
                    }
                }
            }

            // ---------------- ROLES ----------------
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Roles", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    allRoles.forEach { role ->
                        val isSelected = role in selectedRoles
                        preferenceChip(
                            label = role.name,
                            selected = isSelected,
                            enabled = isSelected || selectedRoles.size < 2,
                            onClick = {
                                selectedRoles =
                                    if (isSelected) {
                                        (selectedRoles - role).toMutableSet()
                                    } else if (selectedRoles.size < 2) {
                                        (selectedRoles + role).toMutableSet()
                                    } else {
                                        selectedRoles
                                    }
                            },
                        )
                    }
                }
            }

            // ---------------- SCHEDULE ----------------
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
                            selected = schedule in selectedSchedule,
                            onClick = {
                                selectedSchedule =
                                    if (schedule in selectedSchedule) {
                                        (selectedSchedule - schedule).toMutableSet()
                                    } else {
                                        (selectedSchedule + schedule).toMutableSet()
                                    }
                            },
                        )
                    }
                }
            }

            // ---------------- PLAYSTYLE ----------------
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
                            selected = style in selectedPlaystyle,
                            onClick = {
                                selectedPlaystyle =
                                    if (style in selectedPlaystyle) {
                                        (selectedPlaystyle - style).toMutableSet()
                                    } else {
                                        (selectedPlaystyle + style).toMutableSet()
                                    }
                            },
                        )
                    }
                }
            }
        }
    }
}
