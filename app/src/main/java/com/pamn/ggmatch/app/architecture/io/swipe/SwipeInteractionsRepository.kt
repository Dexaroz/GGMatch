package com.pamn.ggmatch.app.architecture.io.swipe

import com.pamn.ggmatch.app.architecture.model.swipe.SwipeInteractionsProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface SwipeInteractionsRepository {
    suspend fun add(profile: SwipeInteractionsProfile): Result<Unit, AppError>

    suspend fun update(profile: SwipeInteractionsProfile): Result<Unit, AppError>

    suspend fun addOrUpdate(profile: SwipeInteractionsProfile): Result<Unit, AppError>

    suspend fun remove(id: UserId): Result<Unit, AppError>

    suspend fun get(id: UserId): Result<SwipeInteractionsProfile?, AppError>
}
