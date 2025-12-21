package com.pamn.ggmatch.app.architecture.io.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserPhotoUrl
import com.pamn.ggmatch.app.architecture.model.user.UserId
import java.io.ByteArrayOutputStream

class FirestoreBase64ProfileImageStrategy(
    private val profileRepository: ProfileRepository,
) : ProfileImageStrategy {

    override suspend fun save(context: Context, userId: UserId, source: Uri): UserPhotoUrl {
        val bytes = context.contentResolver.openInputStream(source)?.use { it.readBytes() }
            ?: throw IllegalArgumentException("Can't read bytes from uri: $source")

        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: throw IllegalArgumentException("Invalid image")

        val resized = resizeKeepingAspect(bmp, maxSize = 512)

        val out = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 70, out)
        val jpgBytes = out.toByteArray()

        val b64 = Base64.encodeToString(jpgBytes, Base64.NO_WRAP)

        profileRepository.updatePhotoBase64(
            userId = userId,
            photoBase64 = b64,
            photoUrl = "firestore://${userId.value}",
        )

        return UserPhotoUrl.from("firestore://${userId.value}")
            ?: throw IllegalStateException("Couldn't build firestore:// url")
    }

    private fun resizeKeepingAspect(src: Bitmap, maxSize: Int): Bitmap {
        val w = src.width
        val h = src.height
        if (w <= maxSize && h <= maxSize) return src

        val scale = if (w >= h) maxSize.toFloat() / w else maxSize.toFloat() / h
        val newW = (w * scale).toInt().coerceAtLeast(1)
        val newH = (h * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(src, newW, newH, true)
    }
}