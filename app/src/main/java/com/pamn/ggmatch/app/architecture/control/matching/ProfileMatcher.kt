package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.model.preferences.MatchPreferencesProfile
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus

class ProfileMatcher {
    fun matches(
        currentUserPreferences: MatchPreferencesProfile,
        candidate: UserProfile,
    ): Boolean {
        val riotAccount =
            candidate.riotAccount
                ?: return false

        if (riotAccount.verificationStatus != RiotAccountStatus.VERIFIED) {
            return false
        }

        val preferences = candidate.preferences
        val currentPrefs = currentUserPreferences.preferences

        val roleMatch =
            currentPrefs.roles.intersect(preferences.favoriteRoles).isNotEmpty()

        val languageMatch =
            currentPrefs.languages.intersect(preferences.languages).isNotEmpty()

        val playstyleMatch =
            currentPrefs.playstyles.intersect(preferences.playstyle).isNotEmpty()

        val scheduleMatch =
            currentPrefs.schedules.intersect(preferences.playSchedule).isNotEmpty()
        return roleMatch &&
            languageMatch &&
            playstyleMatch &&
            scheduleMatch
    }
}
