package com.pamn.ggmatch.app.architecture.io.matchmaking

import com.pamn.ggmatch.app.architecture.model.matchmaking.MatchPreferencesProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface MatchPreferencesRepository {
    suspend fun add(profile: MatchPreferencesProfile): Result<Unit, AppError>

    suspend fun update(profile: MatchPreferencesProfile): Result<Unit, AppError>

    suspend fun addOrUpdate(profile: MatchPreferencesProfile): Result<Unit, AppError>

    suspend fun remove(id: UserId): Result<Unit, AppError>

    suspend fun get(id: UserId): Result<MatchPreferencesProfile?, AppError>

    suspend fun getAll(): Result<List<MatchPreferencesProfile>, AppError>
}
