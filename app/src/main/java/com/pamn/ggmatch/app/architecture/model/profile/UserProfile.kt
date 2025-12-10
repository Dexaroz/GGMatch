package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.AggregateRoot
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.datetime.Instant

class UserProfile private constructor(
    id: UserId,
    var riotAccount: RiotAccount?,
    var preferences: Preferences,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<UserId>(id) {

    companion object {
        fun createNew(
            id: UserId,
            riotAccount: RiotAccount? = null,
            preferences: Preferences,
            timeProvider: TimeProvider,
        ): UserProfile {
            val now = timeProvider.now()

            return UserProfile(
                id = id,
                riotAccount = riotAccount,
                preferences = preferences,
                createdAt = now,
                updatedAt = now,
            )
        }

        fun fromPersistence(
            id: UserId,
            riotAccount: RiotAccount?,
            favoriteRoles: Set<LolRole>,
            languages: Set<Language>,
            playSchedule: Set<PlaySchedule>,
            playstyle: Set<Playstyle>,
            createdAt: Instant,
            updatedAt: Instant,
        ): UserProfile =
            UserProfile(
                id = id,
                riotAccount = riotAccount,
                preferences =
                    Preferences(
                        favoriteRoles = favoriteRoles,
                        languages = languages,
                        playSchedule = playSchedule,
                        playstyle = playstyle,
                    ),
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
    }

    fun changeRiotAccount(
        newAccount: RiotAccount?,
        timeProvider: TimeProvider,
    ) {
        if (riotAccount == newAccount) return

        riotAccount = newAccount
        updatedAt = timeProvider.now()
    }

    fun updatePreferences(
        newPreferences: Preferences,
        timeProvider: TimeProvider,
    ) {
        if (preferences == newPreferences) return

        preferences = newPreferences
        updatedAt = timeProvider.now()
    }
}
