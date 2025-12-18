package com.pamn.ggmatch.app.architecture.control.matchmaking.commands

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class UpsertMatchPreferencesCommand(
    val userId: UserId,
    val roles: Set<LolRole>,
    val languages: Set<Language>,
    val schedules: Set<PlaySchedule>,
    val playstyles: Set<Playstyle>,
) : Command
