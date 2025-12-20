package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.AggregateRoot
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.datetime.Instant

class UserProfile private constructor(
    id: UserId,
    var username: Username?,
    var riotAccount: RiotAccount?,
    var preferences: Preferences,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<UserId>(id) {
    companion object {
        fun createNew(
            id: UserId,
            timeProvider: TimeProvider,
            username: Username? = null,
            riotAccount: RiotAccount? = null,
            preferences: Preferences = Preferences.default(),
        ): UserProfile {
            val now = timeProvider.now()
            return UserProfile(
                id = id,
                username = username,
                riotAccount = riotAccount,
                preferences = preferences,
                createdAt = now,
                updatedAt = now,
            )
        }

        fun fromPersistence(
            id: UserId,
            username: Username?,
            riotAccount: RiotAccount?,
            preferences: Preferences,
            createdAt: Instant,
            updatedAt: Instant,
        ): UserProfile =
            UserProfile(
                id = id,
                username = username,
                riotAccount = riotAccount,
                preferences = preferences,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
    }

    fun changeUsername(
        newUsername: Username?,
        timeProvider: TimeProvider,
    ) {
        if (username == newUsername) return
        username = newUsername
        updatedAt = timeProvider.now()
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
