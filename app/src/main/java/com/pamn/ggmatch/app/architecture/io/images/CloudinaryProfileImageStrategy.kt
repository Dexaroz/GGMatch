package com.pamn.ggmatch.app.architecture.io.images

import android.content.Context
import android.net.Uri
import com.pamn.ggmatch.app.architecture.model.profile.UserPhotoUrl
import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class CloudinaryProfileImageStrategy(
    private val http: OkHttpClient,
    private val cloudName: String,
    private val uploadPreset: String,
) : ProfileImageStrategy {
    override suspend fun save(
        context: Context,
        userId: UserId,
        source: Uri,
    ): UserPhotoUrl =
        withContext(Dispatchers.IO) {
            val tempFile = uriToTempFile(context, source)

            val body =
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("upload_preset", uploadPreset)
                    .addFormDataPart(
                        "file",
                        tempFile.name,
                        tempFile.asRequestBody("image/*".toMediaTypeOrNull()),
                    )
                    .build()

            val req =
                Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
                    .post(body)
                    .build()

            http.newCall(req).execute().use { res ->
                val str = res.body?.string().orEmpty()
                if (!res.isSuccessful) {
                    throw IllegalStateException("Cloudinary upload failed: ${res.code} $str")
                }
                val secureUrl = JSONObject(str).getString("secure_url")
                UserPhotoUrl.from(secureUrl) ?: error("Invalid secure_url")
            }
        }

    private fun uriToTempFile(
        context: Context,
        uri: Uri,
    ): File {
        val input =
            context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Can't open input stream for uri: $uri")

        val file = File.createTempFile("avatar_", ".jpg", context.cacheDir)
        input.use { ins ->
            file.outputStream().use { outs -> ins.copyTo(outs) }
        }
        return file
    }
}
