package com.pamn.ggmatch.app.architecture.control.profile.commands

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.user.UserId

data class UpsertUserProfileCommand(
    val userId: UserId,
    val riotAccount: RiotAccount?,
    val favoriteRoles: Set<LolRole>,
    val languages: Set<Language>,
    val playSchedule: Set<PlaySchedule>,
    val playstyle: Set<Playstyle>,
)
