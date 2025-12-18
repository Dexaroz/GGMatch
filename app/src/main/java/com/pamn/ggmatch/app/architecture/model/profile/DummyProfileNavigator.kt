package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus
import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

class DummyProfileNavigator : ProfileNavigator {
    private val dummyProfile =
        UserProfile.fromPersistence(
            id = UserId("dummy_1"),
            riotAccount =
                RiotAccount(
                    gameName = "GGMate",
                    tagLine = "PRO",
                    verificationStatus = RiotAccountStatus.VERIFIED,
                    lastVerifiedAt = Instant.DISTANT_PAST,
                ),
            favoriteRoles = setOf(LolRole.MID, LolRole.JUNGLE),
            languages = setOf(Language.ENGLISH, Language.SPANISH),
            playSchedule = setOf(PlaySchedule.NIGHT),
            playstyle = setOf(Playstyle.RANKED),
            createdAt = Instant.DISTANT_PAST,
            updatedAt = Instant.DISTANT_PAST,
        )

    override fun current(): UserProfile = dummyProfile

    override fun next(): UserProfile? = null

    override fun previous(): UserProfile? = null
}
