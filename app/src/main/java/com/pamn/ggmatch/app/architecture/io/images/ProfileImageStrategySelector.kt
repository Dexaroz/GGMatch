package com.pamn.ggmatch.app.architecture.io.images

import com.pamn.ggmatch.app.architecture.model.profile.UserPhotoUrl

class ProfileImageStrategySelector(
    private val cloudinary: ProfileImageStrategy,
    private val firestore: ProfileImageStrategy,
) {
    fun forSaving(useFirestoreInline: Boolean): ProfileImageStrategy =
        if (useFirestoreInline) firestore else cloudinary

    fun isFirestoreInline(photoUrl: UserPhotoUrl?): Boolean =
        photoUrl?.value?.startsWith("firestore://") == true
}