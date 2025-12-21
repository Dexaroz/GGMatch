package com.pamn.ggmatch.app.architecture.io.images

import android.util.Base64
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

object ProfileImageUiModelResolver {
    fun resolve(
        profile: UserProfile,
        photoBase64: String?,
    ): Any? {
        val url = profile.photoUrl?.value ?: return null
        return if (url.startsWith("firestore://")) {
            if (photoBase64.isNullOrBlank()) {
                null
            } else {
                Base64.decode(photoBase64, Base64.DEFAULT)
            }
        } else {
            url
        }
    }
}
