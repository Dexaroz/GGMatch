package com.pamn.ggmatch.app.architecture.io.swipe

import com.pamn.ggmatch.app.architecture.model.swipe.SwipeHistory
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface SwipeHistoryRepository {
    suspend fun add(profile: SwipeHistory): Result<Unit, AppError>

    suspend fun update(profile: SwipeHistory): Result<Unit, AppError>

    suspend fun addOrUpdate(profile: SwipeHistory): Result<Unit, AppError>

    suspend fun remove(id: UserId): Result<Unit, AppError>

    suspend fun get(id: UserId): Result<SwipeHistory?, AppError>
}
