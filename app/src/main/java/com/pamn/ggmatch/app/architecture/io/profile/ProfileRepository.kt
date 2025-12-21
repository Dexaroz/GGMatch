package com.pamn.ggmatch.app.architecture.io.profile

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface ProfileRepository {
    suspend fun get(id: UserId): Result<UserProfile?, AppError>

    suspend fun add(profile: UserProfile): Result<Unit, AppError>

    suspend fun update(profile: UserProfile): Result<Unit, AppError>

    suspend fun addOrUpdate(profile: UserProfile): Result<Unit, AppError>

    suspend fun getAll(): Result<List<UserProfile>, AppError>

    suspend fun updatePhotoBase64(
        userId: UserId,
        photoBase64: String?,
        photoUrl: String?,
    )
}
