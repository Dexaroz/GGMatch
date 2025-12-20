package com.pamn.ggmatch.app.architecture.view.testScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.profile.commands.VerifyRiotAccountCommand
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RiotVerifyTestView(
    modifier: Modifier = Modifier,
) {
    var gameName by rememberSaveable { mutableStateOf("") }
    var tagLine by rememberSaveable { mutableStateOf("") }

    var isLoading by rememberSaveable { mutableStateOf(false) }
    var statusText by rememberSaveable { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier =
            modifier
                .padding(16.dp),
    ) {
        Text("Test Riot SOLOQ (EUW)")

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = gameName,
            onValueChange = { gameName = it },
            label = { Text("Game name (ej: Pepe)") },
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = tagLine,
            onValueChange = { tagLine = it },
            label = { Text("Tag line (ej: EUW)") },
            singleLine = true,
        )

        Spacer(Modifier.height(12.dp))

        Button(
            enabled = !isLoading && gameName.isNotBlank() && tagLine.isNotBlank(),
            onClick = {
                scope.launch {
                    isLoading = true
                    statusText = null

                    val cmd =
                        VerifyRiotAccountCommand(
                            userId = AppContainer.currentUserId,
                            gameName = gameName.trim(),
                            tagLine = tagLine.trim(),
                        )

                    val res = AppContainer.profileController.verifyRiotAccount(cmd)

                    when (res) {
                        is Result.Error -> {
                            isLoading = false
                            statusText = "❌ Error verificando: ${res.error}"
                        }

                        is Result.Ok -> {
                            // Leer perfil actualizado
                            when (val profileRes = AppContainer.profileRepository.get(AppContainer.currentUserId)) {
                                is Result.Error -> {
                                    isLoading = false
                                    statusText = "⚠️ Verificado pero no pude leer perfil: ${profileRes.error}"
                                }

                                is Result.Ok -> {
                                    isLoading = false
                                    val profile = profileRes.value
                                    val riot = profile?.riotAccount
                                    val soloq = riot?.soloq

                                    statusText =
                                        when {
                                            riot == null -> "⚠️ No se guardó riotAccount en perfil (revisa handler/repo)."
                                            soloq == null ->
                                                "✅ ${riot.gameName}#${riot.tagLine}\nSOLOQ: Unranked (sin entrada RANKED_SOLO_5x5)"
                                            else -> {
                                                val div = soloq.division?.let { " $it" }.orEmpty()
                                                val total = soloq.wins + soloq.losses
                                                val wr =
                                                    if (total == 0) 0
                                                    else ((soloq.wins.toDouble() / total.toDouble()) * 100.0).roundToInt()

                                                val streakTxt = if (soloq.hotStreak) "🔥 racha" else ""
                                                val inactiveTxt = if (soloq.inactive) "⏸ inactivo" else ""
                                                val flags =
                                                    listOf(streakTxt, inactiveTxt)
                                                        .filter { it.isNotBlank() }
                                                        .joinToString(" · ")
                                                        .let { if (it.isBlank()) "" else "\n$it" }

                                                "✅ ${riot.gameName}#${riot.tagLine}\n" +
                                                        "SOLOQ: ${soloq.tier}$div · ${soloq.leaguePoints} LP\n" +
                                                        "WR: $wr% (${soloq.wins}-${soloq.losses})$flags"
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            },
        ) {
            Text("Verificar y leer")
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
        }

        if (statusText != null) {
            Text(statusText.orEmpty())
        }
    }
}
