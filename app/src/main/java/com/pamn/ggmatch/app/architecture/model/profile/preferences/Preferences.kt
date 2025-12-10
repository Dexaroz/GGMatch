package com.pamn.ggmatch.app.architecture.model.profile.preferences

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

data class Preferences(
    val favoriteRoles: Set<LolRole>,
    val languages: Set<Language>,
    val playSchedule: Set<PlaySchedule>,
    val playstyle: Set<Playstyle>,
) : ValueObject {

    init {
        require(favoriteRoles.size in 1..MAX_FAVORITE_ROLES) {
            "favoriteRoles must contain between 1 and $MAX_FAVORITE_ROLES roles"
        }

        require(languages.size >= 1) {
            "languages must contain at least 1 language."
        }

        require(playSchedule.size >= 1) {
            "playSchedule must contain at least 1 language."
        }

        require(playstyle.size >= 1) {
            "playstyle must contain at least 1 language."
        }
    }

    companion object {
        const val MAX_FAVORITE_ROLES: Int = 2
    }
}
