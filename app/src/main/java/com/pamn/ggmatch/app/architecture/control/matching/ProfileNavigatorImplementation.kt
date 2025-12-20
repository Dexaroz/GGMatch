package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class ProfileNavigatorImplementation(
    private val repository: ProfileRepository,
    private val profileFilter: ProfileFilter,
    private val preferences: MatchPreferences,
) : ProfileNavigator {

    private var profiles: List<UserProfile> = emptyList()
    private var currentIndex = 0

    suspend fun load(): Result<Unit, AppError> =
        when (val result = repository.getAll()) {
            is Result.Error -> result
            is Result.Ok -> {
                profiles =
                    profileFilter.filter(
                        profiles = result.value,
                        preferences = preferences,
                    )
                currentIndex = 0
                Result.Ok(Unit)
            }
        }

    override fun current(): UserProfile? =
        profiles.getOrNull(currentIndex)

    override fun next(): UserProfile? =
        profiles.getOrNull(currentIndex + 1)
            ?.also { currentIndex++ }
}
