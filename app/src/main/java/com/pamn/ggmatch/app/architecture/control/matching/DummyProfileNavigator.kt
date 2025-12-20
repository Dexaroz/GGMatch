package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.DummyUserProfiles
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile


class DummyProfileNavigator(
    private val currentUserPreferences: MatchPreferences,
) : ProfileNavigator {

    private val profileFilter = ProfileFilter()

    private val profiles: List<UserProfile> =
        DummyUserProfiles.all

    private val filteredProfiles: List<UserProfile> =
        profileFilter.filter(profiles, currentUserPreferences)

    private var currentIndex = 0

    override fun current(): UserProfile? =
        filteredProfiles.getOrNull(currentIndex)

    override fun next(): UserProfile? {
        if (filteredProfiles.isEmpty()) return null
        val nextIndex = currentIndex + 1
        return filteredProfiles.getOrNull(nextIndex)
            ?.also { currentIndex = nextIndex }
    }
}


