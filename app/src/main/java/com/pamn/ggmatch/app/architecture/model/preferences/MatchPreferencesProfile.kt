package com.pamn.ggmatch.app.architecture.model.preferences
import com.pamn.ggmatch.app.architecture.model.preferences.preferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.AggregateRoot
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.datetime.Instant

class MatchPreferencesProfile private constructor(
    id: UserId,
    var preferences: MatchPreferences,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<UserId>(id) {
    companion object {
        fun createNew(
            userId: UserId,
            preferences: MatchPreferences,
            timeProvider: TimeProvider,
        ): MatchPreferencesProfile {
            val now = timeProvider.now()
            return MatchPreferencesProfile(
                id = userId,
                preferences = preferences,
                createdAt = now,
                updatedAt = now,
            )
        }

        fun fromPersistence(
            userId: UserId,
            preferences: MatchPreferences,
            createdAt: Instant,
            updatedAt: Instant,
        ) = MatchPreferencesProfile(
            id = userId,
            preferences = preferences,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    fun update(
        newPreferences: MatchPreferences,
        timeProvider: TimeProvider,
    ) {
        if (preferences == newPreferences) return
        preferences = newPreferences
        updatedAt = timeProvider.now()
    }
}
