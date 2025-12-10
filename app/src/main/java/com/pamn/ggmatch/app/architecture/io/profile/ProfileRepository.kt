package com.pamn.ggmatch.app.architecture.io.profile

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result


interface ProfileRepository {
    suspend fun add(profile: UserProfile): Result<Unit, AppError>
    suspend fun update(profile: UserProfile): Result<Unit, AppError>
    suspend fun addOrUpdate(profile: UserProfile): Result<Unit, AppError>
    suspend fun remove(id: UserId): Result<Unit, AppError>
    suspend fun get(id: UserId): Result<UserProfile?, AppError>
    suspend fun getAll(): Result<List<UserProfile>, AppError>
}
