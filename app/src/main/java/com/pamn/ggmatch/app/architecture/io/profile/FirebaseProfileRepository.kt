package com.pamn.ggmatch.app.architecture.io.profile

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Instant

class FirebaseProfileRepository(
    firestore: FirebaseFirestore,
) : FirebaseRepository<UserId, UserProfile>(
    firestore = firestore,
    collectionName = "profiles",
),
    ProfileRepository {

    override fun getId(entity: UserProfile): UserId = entity.id

    override fun idToString(id: UserId): String = id.value

    override fun stringToId(id: String): UserId = UserId(id)

    override fun toDocument(entity: UserProfile): Map<String, Any?> {
        val riot = entity.riotAccount

        return mapOf(
            "riotGameName" to riot?.gameName,
            "riotTagLine" to riot?.tagLine,
            "riotVerificationStatus" to riot?.verificationStatus?.name,
            "riotLastVerifiedAtEpochMs" to riot?.lastVerifiedAt?.toEpochMilliseconds(),

            "favoriteRoles" to entity.preferences.favoriteRoles.map { it.name },
            "languages" to entity.preferences.languages.map { it.name },
            "playSchedule" to entity.preferences.playSchedule.map { it.name },
            "playstyle" to entity.preferences.playstyle.map { it.name },

            "createdAtEpochMs" to entity.createdAt.toEpochMilliseconds(),
            "updatedAtEpochMs" to entity.updatedAt.toEpochMilliseconds(),
        )
    }

    override fun fromDocument(
        id: UserId,
        doc: DocumentSnapshot,
    ): UserProfile {
        val riotGameName = doc.getString("riotGameName")
        val riotTagLine = doc.getString("riotTagLine")

        val verificationStatusName = doc.getString("riotVerificationStatus")
        val verificationStatus =
            verificationStatusName
                ?.let { name ->
                    runCatching { RiotAccountStatus.valueOf(name) }
                        .getOrElse { RiotAccountStatus.UNVERIFIED }
                }
                ?: RiotAccountStatus.UNVERIFIED

        val lastVerifiedAtMs = doc.getLong("riotLastVerifiedAtEpochMs")
        val lastVerifiedAt =
            lastVerifiedAtMs?.let { ms -> Instant.fromEpochMilliseconds(ms) }

        val riotAccount =
            if (!riotGameName.isNullOrBlank() && !riotTagLine.isNullOrBlank()) {
                RiotAccount(
                    gameName = riotGameName,
                    tagLine = riotTagLine,
                    verificationStatus = verificationStatus,
                    lastVerifiedAt = lastVerifiedAt,
                )
            } else {
                null
            }

        fun rolesFromDoc(field: String): Set<LolRole> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { LolRole.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun languagesFromDoc(field: String): Set<Language> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { Language.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun scheduleFromDoc(field: String): Set<PlaySchedule> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { PlaySchedule.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        fun playstyleFromDoc(field: String): Set<Playstyle> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { value ->
                (value as? String)?.let { name ->
                    runCatching { Playstyle.valueOf(name) }.getOrNull()
                }
            }.toSet()
        }

        val preferences =
            Preferences(
                favoriteRoles = rolesFromDoc("favoriteRoles"),
                languages = languagesFromDoc("languages"),
                playSchedule = scheduleFromDoc("playSchedule"),
                playstyle = playstyleFromDoc("playstyle"),
            )

        val createdAtMs =
            doc.getLong("createdAtEpochMs")
                ?: error("Missing 'createdAtEpochMs' for profile ${id.value}")
        val updatedAtMs =
            doc.getLong("updatedAtEpochMs")
                ?: createdAtMs

        return UserProfile.fromPersistence(
            id = id,
            riotAccount = riotAccount,
            favoriteRoles = preferences.favoriteRoles,
            languages = preferences.languages,
            playSchedule = preferences.playSchedule,
            playstyle = preferences.playstyle,
            createdAt = Instant.fromEpochMilliseconds(createdAtMs),
            updatedAt = Instant.fromEpochMilliseconds(updatedAtMs),
        )
    }

    override suspend fun addOrUpdate(profile: UserProfile): Result<Unit, AppError> =
        when (val existing = get(profile.id)) {
            is Result.Error -> existing
            is Result.Ok ->
                if (existing.value == null) {
                    add(profile)
                } else {
                    update(profile)
                }
        }
}
