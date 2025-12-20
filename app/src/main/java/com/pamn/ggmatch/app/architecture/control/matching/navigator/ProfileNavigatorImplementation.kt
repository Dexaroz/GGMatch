package com.pamn.ggmatch.app.architecture.control.matching.navigator

import com.pamn.ggmatch.app.architecture.control.matching.tools.ProfileFilter
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeType
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class ProfileNavigatorImplementation(
    private val repository: ProfileRepository,
    private val swipeRepository: SwipeHistoryRepository,
    private val profileFilter: ProfileFilter,
    private val preferences: MatchPreferences,
) : ProfileNavigator {
    private var profiles: List<UserProfile> = emptyList()
    private var currentIndex = 0

    override suspend fun load(): Result<Unit, AppError> {
        val allProfilesResult = repository.getAll()

        if (allProfilesResult is Result.Error) return allProfilesResult

        val allProfiles = (allProfilesResult as Result.Ok).value

        val myHistoryResult = swipeRepository.get(preferences.id)

        val excludedIds = when (myHistoryResult) {
            is Result.Ok -> {
                myHistoryResult.value?.items
                    ?.filter { it.value.type == SwipeType.LIKE }
                    ?.keys
                    ?.map { it.value }
                    ?.toSet() ?: emptySet()
            }
            is Result.Error -> emptySet()
        }

        profiles = profileFilter.filter(
            profiles = allProfiles,
            preferences = preferences,
            currentUserId = preferences.id.value,
            excludedIds = excludedIds
        )

        currentIndex = 0
        return Result.Ok(Unit)
    }

    override fun current(): UserProfile? = profiles.getOrNull(currentIndex)

    override fun next(): UserProfile? =
        profiles.getOrNull(currentIndex + 1)
            ?.also { currentIndex++ }
}