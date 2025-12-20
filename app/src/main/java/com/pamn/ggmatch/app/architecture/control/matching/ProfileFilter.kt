package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

class ProfileFilter(
    private val profileMatcher: ProfileMatcher = ProfileMatcher(),
) {

    fun filter(
        profiles: List<UserProfile>,
        preferences: MatchPreferences,
    ): List<UserProfile> =
        profiles.filter { profileMatcher.matches(preferences, it) }
}
