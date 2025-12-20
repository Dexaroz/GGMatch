package com.pamn.ggmatch.app.architecture.io.preferences

import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface MatchPreferencesRepository {
    suspend fun add(profile: MatchPreferences): Result<Unit, AppError>

    suspend fun update(profile: MatchPreferences): Result<Unit, AppError>

    suspend fun addOrUpdate(profile: MatchPreferences): Result<Unit, AppError>

    suspend fun remove(id: UserId): Result<Unit, AppError>

    suspend fun get(id: UserId): Result<MatchPreferences?, AppError>

    suspend fun getAll(): Result<List<MatchPreferences>, AppError>
}
