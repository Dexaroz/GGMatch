package com.pamn.ggmatch.app.architecture.model.matchPreferences

import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.datetime.Instant
import org.junit.Test

private class FixedTimeProvider(private val instant: Instant) : TimeProvider {
    override fun now(): Instant = instant
}

class MatchPreferencesTest {
    private val userId = UserId("user-456")
    private val initialTime = Instant.parse("2025-01-01T10:00:00Z")
    private val defaultPreferences = Preferences.default()

    @Test
    fun `createNew should initialize with correct timestamps and values`() {
        val timeProvider = FixedTimeProvider(initialTime)

        val matchPrefs =
            MatchPreferences.createNew(
                userId = userId,
                preferences = defaultPreferences,
                timeProvider = timeProvider,
            )

        assertEquals(userId, matchPrefs.id)
        assertEquals(defaultPreferences, matchPrefs.preferences)
        assertEquals(initialTime, matchPrefs.createdAt)
        assertEquals(initialTime, matchPrefs.updatedAt)
    }

    @Test
    fun `update should change preferences and updatedAt when values are different`() {
        val createTime = Instant.parse("2025-01-01T10:00:00Z")
        val updateTime = Instant.parse("2025-01-01T12:00:00Z")

        val matchPrefs = MatchPreferences.createNew(userId, defaultPreferences, FixedTimeProvider(createTime))

        val newPreferences =
            defaultPreferences.copy(
                roles = setOf(LolRole.TOP),
            )

        matchPrefs.update(newPreferences, FixedTimeProvider(updateTime))

        assertEquals(newPreferences, matchPrefs.preferences)
        assertEquals(updateTime, matchPrefs.updatedAt)
        assertEquals(createTime, matchPrefs.createdAt)
    }

    @Test
    fun `update should be idempotent and not change updatedAt if preferences are the same`() {
        val createTime = Instant.parse("2025-01-01T10:00:00Z")
        val updateTime = Instant.parse("2025-01-01T12:00:00Z")

        val matchPrefs = MatchPreferences.createNew(userId, defaultPreferences, FixedTimeProvider(createTime))

        // Update with the same exact object
        matchPrefs.update(defaultPreferences, FixedTimeProvider(updateTime))

        assertEquals(createTime, matchPrefs.updatedAt)
    }

    @Test
    fun `fromPersistence should restore state exactly`() {
        val createdAt = Instant.parse("2025-01-01T10:00:00Z")
        val updatedAt = Instant.parse("2025-01-02T10:00:00Z")

        val matchPrefs =
            MatchPreferences.fromPersistence(
                userId = userId,
                preferences = defaultPreferences,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )

        assertEquals(userId, matchPrefs.id)
        assertEquals(createdAt, matchPrefs.createdAt)
        assertEquals(updatedAt, matchPrefs.updatedAt)
    }
}

class PreferencesTest {
    @Test
    fun `default preferences should contain all possible values`() {
        val default = Preferences.default()

        assertTrue(default.roles.containsAll(LolRole.entries.toSet()))
        assertTrue(default.languages.containsAll(Language.entries.toSet()))
        assertTrue(default.schedules.containsAll(PlaySchedule.entries.toSet()))
        assertTrue(default.playstyles.containsAll(Playstyle.entries.toSet()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should reject empty roles`() {
        Preferences(
            roles = emptySet(),
            languages = setOf(Language.ENGLISH),
            schedules = setOf(PlaySchedule.MORNING),
            playstyles = setOf(Playstyle.RANKED),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should reject empty languages`() {
        Preferences(
            roles = setOf(LolRole.MID),
            languages = emptySet(),
            schedules = setOf(PlaySchedule.NIGHT),
            playstyles = setOf(Playstyle.CASUAL),
        )
    }

    @Test
    fun `valid preferences should be accepted`() {
        val roles = setOf(LolRole.JUNGLE, LolRole.SUPPORT)
        val prefs =
            Preferences(
                roles = roles,
                languages = setOf(Language.SPANISH),
                schedules = setOf(PlaySchedule.AFTERNOON),
                playstyles = setOf(Playstyle.RANKED),
            )

        assertEquals(roles, prefs.roles)
    }
}
