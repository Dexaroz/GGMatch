package com.pamn.ggmatch.app.architecture.control.matching.tools

import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

class ProfileFilter(
    private val profileMatcher: ProfileMatcher = ProfileMatcher(),
) {
    fun filter(
        profiles: List<UserProfile>,
        preferences: MatchPreferences,
        currentUserId: String,
    ): List<UserProfile> =
        profiles.filter {
            profileMatcher.matches(currentUserId, preferences, it)
        }
}
