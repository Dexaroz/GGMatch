package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccountStatus
import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

object DummyUserProfiles {
    val all: List<UserProfile> =
        listOf(
            UserProfile.fromPersistence(
                id = UserId("dummy_1"),
                username = Username("dumy_1"),
                riotAccount =
                    RiotAccount(
                        gameName = "GGMate",
                        tagLine = "PRO",
                        verificationStatus = RiotAccountStatus.VERIFIED,
                        lastVerifiedAt = Instant.DISTANT_PAST,
                    ),
                preferences =
                    Preferences(
                        favoriteRoles = setOf(LolRole.MID, LolRole.JUNGLE),
                        languages = setOf(Language.ENGLISH, Language.SPANISH),
                        playSchedule = setOf(PlaySchedule.NIGHT),
                        playstyle = setOf(Playstyle.RANKED),
                    ),
                createdAt = Instant.DISTANT_PAST,
                updatedAt = Instant.DISTANT_PAST,
            ),
            UserProfile.fromPersistence(
                id = UserId("dummy_2"),
                username = Username("dumy_2"),
                riotAccount =
                    RiotAccount(
                        gameName = "TopKing",
                        tagLine = "EUW",
                        verificationStatus = RiotAccountStatus.VERIFIED,
                        lastVerifiedAt = Instant.DISTANT_PAST,
                    ),
                preferences =
                    Preferences(
                        favoriteRoles = setOf(LolRole.TOP),
                        languages = setOf(Language.ENGLISH),
                        playSchedule = setOf(PlaySchedule.AFTERNOON),
                        playstyle = setOf(Playstyle.CASUAL),
                    ),
                createdAt = Instant.DISTANT_PAST,
                updatedAt = Instant.DISTANT_PAST,
            ),
            UserProfile.fromPersistence(
                id = UserId("dummy_3"),
                username = Username("dummy_3"),
                riotAccount =
                    RiotAccount(
                        gameName = "BotCarry",
                        tagLine = "ADC",
                        verificationStatus = RiotAccountStatus.UNVERIFIED,
                        lastVerifiedAt = Instant.DISTANT_PAST,
                    ),
                preferences =
                    Preferences(
                        favoriteRoles = setOf(LolRole.ADC, LolRole.SUPPORT),
                        languages = setOf(Language.SPANISH),
                        playSchedule = setOf(PlaySchedule.NIGHT),
                        playstyle = setOf(Playstyle.RANKED),
                    ),
                createdAt = Instant.DISTANT_PAST,
                updatedAt = Instant.DISTANT_PAST,
            ),
        )
}
