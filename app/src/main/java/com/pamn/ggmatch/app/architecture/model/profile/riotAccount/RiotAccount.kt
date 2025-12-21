package com.pamn.ggmatch.app.architecture.model.profile.riotAccount

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject
import kotlinx.datetime.Instant

data class RiotAccount(
    val gameName: String,
    val tagLine: String,
    val puuid: String? = null,
    val soloq: SoloqStats? = null,
    val verificationStatus: RiotAccountStatus = RiotAccountStatus.UNVERIFIED,
    val lastVerifiedAt: Instant? = null,
) : ValueObject {
    init {
        val trimmedName = gameName.trim()
        val trimmedTag = tagLine.trim()

        require(trimmedName.isNotEmpty()) { "Riot game name cannot be blank" }
        require(trimmedName.length <= 16) { "Riot game name must be at most 16 characters" }

        require(trimmedTag.isNotEmpty()) { "Riot tag cannot be blank" }
        require(trimmedTag.length <= 10) { "Riot tag must be at most 10 characters" }
    }

    fun markVerified(
        at: Instant,
        puuid: String,
        soloq: SoloqStats?,
    ): RiotAccount =
        copy(
            puuid = puuid,
            soloq = soloq,
            verificationStatus = RiotAccountStatus.VERIFIED,
            lastVerifiedAt = at,
        )
}
