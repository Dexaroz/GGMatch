package com.pamn.ggmatch.app.architecture.io.images

import android.content.Context
import android.net.Uri
import com.pamn.ggmatch.app.architecture.model.profile.UserPhotoUrl
import com.pamn.ggmatch.app.architecture.model.user.UserId

interface ProfileImageStrategy {
    suspend fun save(
        context: Context,
        userId: UserId,
        source: Uri,
    ): UserPhotoUrl
}
