package com.pamn.ggmatch.app.architecture.control.matching.tools

import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

class ProfileMatcher {
    fun matches(
        currentUserId: String,
        currentUserPreferences: MatchPreferences,
        candidate: UserProfile,
        excludedIds: Set<String>,
    ): Boolean {
        if (candidate.id.value == currentUserId || excludedIds.contains(candidate.id.value)) {
            return false
        }
        /*
        val riotAccount =
            candidate.riotAccount
                ?: return false

        if (riotAccount.verificationStatus != RiotAccountStatus.VERIFIED) {
            return false
        }
         */

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
