package com.pamn.ggmatch.app.architecture.io.profile

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.Username
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.SoloqStats
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant

class FirebaseProfileRepository(
    private val firestore: FirebaseFirestore,
) : ProfileRepository {
    private val profilesCol = firestore.collection("profiles")

    private fun coreDoc(userId: UserId) = profilesCol.document(userId.value)

    private fun prefsDoc(userId: UserId) = coreDoc(userId).collection("preferences").document("main")

    private fun riotDoc(userId: UserId) = coreDoc(userId).collection("riot").document("main")

    override suspend fun get(id: UserId): Result<UserProfile?, AppError> {
        return try {
            val coreSnap = coreDoc(id).get().await()
            if (!coreSnap.exists()) return Result.Ok(null)

            val usernameRaw = coreSnap.getString("username")
            val username = usernameRaw?.takeIf { it.isNotBlank() }?.let { Username(it) }

            val photoUrl = coreSnap.getString("photoUrl")

            val createdAtMs =
                coreSnap.getLong("createdAtEpochMs")
                    ?: error("Missing 'createdAtEpochMs' for profile ${id.value}")
            val updatedAtMs = coreSnap.getLong("updatedAtEpochMs") ?: createdAtMs

            val prefsSnap = prefsDoc(id).get().await()
            val preferences =
                if (!prefsSnap.exists()) {
                    Preferences.default()
                } else {
                    val roles = enumSetFromDoc<LolRole>(prefsSnap.get("favoriteRoles"))
                    val langs = enumSetFromDoc<Language>(prefsSnap.get("languages"))
                    val schedules = enumSetFromDoc<PlaySchedule>(prefsSnap.get("playSchedule"))
                    val styles = enumSetFromDoc<Playstyle>(prefsSnap.get("playstyle"))

                    runCatching {
                        Preferences(
                            favoriteRoles = roles,
                            languages = langs,
                            playSchedule = schedules,
                            playstyle = styles,
                        )
                    }.getOrElse { Preferences.default() }
                }

            val riotSnap = riotDoc(id).get().await()
            val riotAccount =
                if (!riotSnap.exists()) {
                    null
                } else {
                    val gameName = riotSnap.getString("gameName")
                    val tagLine = riotSnap.getString("tagLine")
                    if (gameName.isNullOrBlank() || tagLine.isNullOrBlank()) {
                        null
                    } else {
                        val puuid = riotSnap.getString("puuid")

                        val statusName = riotSnap.getString("verificationStatus")
                        val status =
                            statusName
                                ?.let {
                                    runCatching { RiotAccountStatus.valueOf(it) }
                                        .getOrElse { RiotAccountStatus.UNVERIFIED }
                                }
                                ?: RiotAccountStatus.UNVERIFIED

                        val lastVerifiedAtMs = riotSnap.getLong("lastVerifiedAtEpochMs")
                        val lastVerifiedAt = lastVerifiedAtMs?.let { Instant.fromEpochMilliseconds(it) }

                        val soloqTier = riotSnap.getString("soloqTier")
                        val soloqDivision = riotSnap.getString("soloqDivision")
                        val soloqLp = riotSnap.getLong("soloqLp")?.toInt()
                        val soloqWins = riotSnap.getLong("soloqWins")?.toInt()
                        val soloqLosses = riotSnap.getLong("soloqLosses")?.toInt()
                        val soloqHotStreak = riotSnap.getBoolean("soloqHotStreak")
                        val soloqInactive = riotSnap.getBoolean("soloqInactive")

                        val soloq =
                            if (!soloqTier.isNullOrBlank() &&
                                soloqLp != null && soloqWins != null && soloqLosses != null &&
                                soloqHotStreak != null && soloqInactive != null
                            ) {
                                SoloqStats(
                                    tier = soloqTier,
                                    division = soloqDivision,
                                    leaguePoints = soloqLp,
                                    wins = soloqWins,
                                    losses = soloqLosses,
                                    hotStreak = soloqHotStreak,
                                    inactive = soloqInactive,
                                )
                            } else {
                                null
                            }

                        RiotAccount(
                            gameName = gameName,
                            tagLine = tagLine,
                            puuid = puuid,
                            soloq = soloq,
                            verificationStatus = status,
                            lastVerifiedAt = lastVerifiedAt,
                        )
                    }
                }

            Result.Ok(
                UserProfile.fromPersistence(
                    id = id,
                    username = username,
                    photoUrl = photoUrl,
                    riotAccount = riotAccount,
                    preferences = preferences,
                    createdAt = Instant.fromEpochMilliseconds(createdAtMs),
                    updatedAt = Instant.fromEpochMilliseconds(updatedAtMs),
                ),
            )
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to load user profile ${id.value}", e))
        }
    }

    override suspend fun updatePhotoBase64(
        userId: UserId,
        photoBase64: String?,
        photoUrl: String?,
    ) {
        firestore.collection("profiles")
            .document(userId.value)
            .update(
                mapOf(
                    "photoBase64" to photoBase64,
                    "photoUrl" to photoUrl,
                ),
            )
            .await()
    }

    override suspend fun add(profile: UserProfile): Result<Unit, AppError> = upsertInternal(profile, merge = false)

    override suspend fun update(profile: UserProfile): Result<Unit, AppError> = upsertInternal(profile, merge = true)

    override suspend fun addOrUpdate(profile: UserProfile): Result<Unit, AppError> =
        when (val existing = get(profile.id)) {
            is Result.Error -> existing
            is Result.Ok -> if (existing.value == null) add(profile) else update(profile)
        }

    private suspend fun upsertInternal(
        profile: UserProfile,
        merge: Boolean,
    ): Result<Unit, AppError> =
        try {
            val batch = firestore.batch()

            val coreData =
                mapOf(
                    "username" to profile.username?.value,
                    "photoUrl" to profile.photoUrl?.value,
                    "createdAtEpochMs" to profile.createdAt.toEpochMilliseconds(),
                    "updatedAtEpochMs" to profile.updatedAt.toEpochMilliseconds(),
                )

            val prefsData =
                mapOf(
                    "favoriteRoles" to profile.preferences.favoriteRoles.map { it.name },
                    "languages" to profile.preferences.languages.map { it.name },
                    "playSchedule" to profile.preferences.playSchedule.map { it.name },
                    "playstyle" to profile.preferences.playstyle.map { it.name },
                )

            val riot = profile.riotAccount
            val soloq = riot?.soloq

            val riotData =
                mapOf(
                    "gameName" to riot?.gameName,
                    "tagLine" to riot?.tagLine,
                    "puuid" to riot?.puuid,
                    "verificationStatus" to riot?.verificationStatus?.name,
                    "lastVerifiedAtEpochMs" to riot?.lastVerifiedAt?.toEpochMilliseconds(),
                    "soloqTier" to soloq?.tier,
                    "soloqDivision" to soloq?.division,
                    "soloqLp" to soloq?.leaguePoints,
                    "soloqWins" to soloq?.wins,
                    "soloqLosses" to soloq?.losses,
                    "soloqHotStreak" to soloq?.hotStreak,
                    "soloqInactive" to soloq?.inactive,
                )

            if (merge) {
                batch.set(coreDoc(profile.id), coreData, SetOptions.merge())
                batch.set(prefsDoc(profile.id), prefsData, SetOptions.merge())
                batch.set(riotDoc(profile.id), riotData, SetOptions.merge())
            } else {
                batch.set(coreDoc(profile.id), coreData)
                batch.set(prefsDoc(profile.id), prefsData)
                batch.set(riotDoc(profile.id), riotData)
            }

            batch.commit().await()
            Result.Ok(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to save user profile ${profile.id.value}", e))
        }

    override suspend fun getAll(): Result<List<UserProfile>, AppError> {
        return try {
            val coreSnaps = profilesCol.get().await()
            val out = mutableListOf<UserProfile>()

            for (doc in coreSnaps.documents) {
                val userId = UserId(doc.id)

                when (val one = get(userId)) {
                    is Result.Error -> return one
                    is Result.Ok -> one.value?.let(out::add)
                }
            }

            Result.Ok(out)
        } catch (e: Exception) {
            Result.Error(AppError.Unexpected("Failed to load profiles", e))
        }
    }

    private inline fun <reified E : Enum<E>> enumSetFromDoc(raw: Any?): Set<E> {
        val list = raw as? List<*> ?: emptyList<Any?>()
        return list.mapNotNull { v ->
            (v as? String)?.let { name ->
                runCatching { enumValueOf<E>(name) }.getOrNull()
            }
        }.toSet()
    }
}
