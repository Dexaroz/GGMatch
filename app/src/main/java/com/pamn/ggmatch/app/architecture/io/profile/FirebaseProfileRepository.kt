package com.pamn.ggmatch.app.architecture.io.profile

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.model.user.UserStatus
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
            "riotVerified" to riot?.lastVerifiedAt,

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
        val riotVerified = doc.getBoolean("riotVerified") ?: false

        val riotAccount =
            if (!riotGameName.isNullOrBlank() && !riotTagLine.isNullOrBlank()) {
                RiotAccount(
                    gameName = riotGameName,
                    tagLine = riotTagLine,
                    lastVerifiedAt = riotVerified,
                )
            } else {
                null
            }

        fun rolesFromDoc(field: String): Set<LolRole> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { name ->
                (name as? String)?.let {
                    runCatching { LolRole.valueOf(it) }.getOrNull()
                }
            }.toSet()
        }

        fun languagesFromDoc(field: String): Set<Language> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { name ->
                (name as? String)?.let {
                    runCatching { Language.valueOf(it) }.getOrNull()
                }
            }.toSet()
        }

        fun scheduleFromDoc(field: String): Set<PlaySchedule> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { name ->
                (name as? String)?.let {
                    runCatching { PlaySchedule.valueOf(it) }.getOrNull()
                }
            }.toSet()
        }

        fun playstyleFromDoc(field: String): Set<Playstyle> {
            val raw = doc.get(field) as? List<*> ?: emptyList<Any?>()
            return raw.mapNotNull { name ->
                (name as? String)?.let {
                    runCatching { Playstyle.valueOf(it) }.getOrNull()
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
}
