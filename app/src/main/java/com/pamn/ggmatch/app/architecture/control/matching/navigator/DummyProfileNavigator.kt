package com.pamn.ggmatch.app.architecture.control.matching.navigator

import com.pamn.ggmatch.app.architecture.control.matching.tools.ProfileFilter
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.DummyUserProfiles
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class DummyProfileNavigator(
    private val currentUserPreferences: MatchPreferences,
) : ProfileNavigator {
    private val profileFilter = ProfileFilter()

    private val profiles: List<UserProfile> =
        DummyUserProfiles.all

    private var filteredProfiles: List<UserProfile> =
        profileFilter.filter(profiles, currentUserPreferences)

    private var currentIndex = 0

    override fun current(): UserProfile? = filteredProfiles.getOrNull(currentIndex)

    override fun next(): UserProfile? {
        if (filteredProfiles.isEmpty()) return null
        val nextIndex = currentIndex + 1
        return filteredProfiles.getOrNull(nextIndex)
            ?.also { currentIndex = nextIndex }
    }
    override suspend fun load(): Result<Unit, AppError> {
        filteredProfiles = profileFilter.filter(
            profiles = DummyUserProfiles.all,
            preferences = currentUserPreferences
        )
        currentIndex = 0

        // Devolvemos éxito manual
        return Result.Ok(Unit)
    }

}
