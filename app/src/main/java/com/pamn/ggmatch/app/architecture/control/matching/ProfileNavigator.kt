package com.pamn.ggmatch.app.architecture.control.matching

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface ProfileNavigator {
    suspend fun load(): Result<Unit, AppError>
    fun current(): UserProfile?

    fun next(): UserProfile?
}
