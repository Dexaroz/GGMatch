package com.pamn.ggmatch.app.architecture.model.matchPreferences
import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.AggregateRoot
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import kotlinx.datetime.Instant

class MatchPreferences private constructor(
    id: UserId,
    var preferences: Preferences,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<UserId>(id) {
    companion object {
        fun createNew(
            userId: UserId,
            preferences: Preferences,
            timeProvider: TimeProvider,
        ): MatchPreferences {
            val now = timeProvider.now()
            return MatchPreferences(
                id = userId,
                preferences = preferences,
                createdAt = now,
                updatedAt = now,
            )
        }

        fun fromPersistence(
            userId: UserId,
            preferences: Preferences,
            createdAt: Instant,
            updatedAt: Instant,
        ) = MatchPreferences(
            id = userId,
            preferences = preferences,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    fun update(
        newPreferences: Preferences,
        timeProvider: TimeProvider,
    ) {
        if (preferences == newPreferences) return
        preferences = newPreferences
        updatedAt = timeProvider.now()
    }
}
