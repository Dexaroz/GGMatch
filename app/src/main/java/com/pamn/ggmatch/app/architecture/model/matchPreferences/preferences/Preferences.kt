package com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

data class Preferences(
    val roles: Set<LolRole>,
    val languages: Set<Language>,
    val schedules: Set<PlaySchedule>,
    val playstyles: Set<Playstyle>,
) : ValueObject {
    init {
        require(roles.isNotEmpty())
        require(languages.isNotEmpty())
        require(schedules.isNotEmpty())
        require(playstyles.isNotEmpty())
    }

    companion object {
        fun default(): Preferences {
            // Aseguramos que cada conjunto contenga al menos un valor.
            return Preferences(
                roles = LolRole.entries.toSet(),
                languages = Language.entries.toSet(),
                schedules = PlaySchedule.entries.toSet(),
                playstyles = Playstyle.entries.toSet(),
            )
        }
    }
}
