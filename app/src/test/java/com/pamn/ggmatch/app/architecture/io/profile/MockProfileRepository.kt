package com.pamn.ggmatch.app.test.architecture.io.profile

import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class MockProfileRepository(
    private val timeProvider: TimeProvider,
) : ProfileRepository {
    private fun createRiotAccount(name: String) =
        RiotAccount(
            gameName = name,
            tagLine = "EUW",
        )

    private val mockProfiles: Map<UserId, UserProfile> by lazy {
        mapOf(
            UserId("U_01_BUSCADOR") to
                UserProfile.createNew(
                    id = UserId("U_01_BUSCADOR"),
                    riotAccount = createRiotAccount("MidKing"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.MID, LolRole.SUPPORT),
                            languages = setOf(Language.SPANISH, Language.ENGLISH),
                            playSchedule = setOf(PlaySchedule.NIGHT, PlaySchedule.AFTERNOON),
                            playstyle = setOf(Playstyle.RANKED, Playstyle.FLEX),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_02_MAX_MATCH") to
                UserProfile.createNew(
                    id = UserId("U_02_MAX_MATCH"),
                    riotAccount = createRiotAccount("TopCarry"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.TOP, LolRole.MID),
                            languages = setOf(Language.SPANISH, Language.ENGLISH),
                            playSchedule = setOf(PlaySchedule.NIGHT, PlaySchedule.AFTERNOON),
                            playstyle = setOf(Playstyle.RANKED, Playstyle.FLEX),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_03_MEDIO_ROL_LANG") to
                UserProfile.createNew(
                    id = UserId("U_03_MEDIO_ROL_LANG"),
                    riotAccount = createRiotAccount("MidLover"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.MID),
                            languages = setOf(Language.ENGLISH),
                            playSchedule = setOf(PlaySchedule.MORNING),
                            playstyle = setOf(Playstyle.ARAM),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_04_BAJO_LANG") to
                UserProfile.createNew(
                    id = UserId("U_04_BAJO_LANG"),
                    riotAccount = createRiotAccount("BotMain"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.ADC),
                            languages = setOf(Language.SPANISH, Language.FRENCH),
                            playSchedule = setOf(PlaySchedule.AFTERNOON),
                            playstyle = setOf(Playstyle.CASUAL),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_05_TEST_ROL") to
                UserProfile.createNew(
                    id = UserId("U_05_TEST_ROL"),
                    riotAccount = createRiotAccount("JungleGank"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.JUNGLE),
                            languages = setOf(Language.SPANISH, Language.ENGLISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.CLASH),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_06_NO_MATCH") to
                UserProfile.createNew(
                    id = UserId("U_06_NO_MATCH"),
                    riotAccount = createRiotAccount("GermanOnly"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.TOP),
                            languages = setOf(Language.GERMAN),
                            playSchedule = setOf(PlaySchedule.MORNING),
                            playstyle = setOf(Playstyle.ARAM),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_07_INCOMPLETO") to
                UserProfile.createNew(
                    id = UserId("U_07_INCOMPLETO"),
                    riotAccount = createRiotAccount("NoRole"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.MID),
                            languages = setOf(Language.SPANISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.FLEX),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_08_NO_RIOT") to
                UserProfile.createNew(
                    id = UserId("U_08_NO_RIOT"),
                    riotAccount = null,
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.MID),
                            languages = setOf(Language.SPANISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.RANKED),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_09_SELF") to
                UserProfile.createNew(
                    id = UserId("U_09_SELF"),
                    riotAccount = createRiotAccount("SelfMatch"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.MID),
                            languages = setOf(Language.SPANISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.RANKED),
                        ),
                    timeProvider = timeProvider,
                ),
            UserId("U_10_BROAD_LANG") to
                UserProfile.createNew(
                    id = UserId("U_10_BROAD_LANG"),
                    riotAccount = createRiotAccount("GlobalPlayer"),
                    preferences =
                        Preferences(
                            favoriteRoles = setOf(LolRole.JUNGLE, LolRole.TOP),
                            languages = setOf(Language.ENGLISH, Language.FRENCH, Language.GERMAN),
                            playSchedule = setOf(PlaySchedule.MORNING, PlaySchedule.AFTERNOON),
                            playstyle = setOf(Playstyle.CASUAL, Playstyle.CLASH),
                        ),
                    timeProvider = timeProvider,
                ),
        )
    }

    override suspend fun get(id: UserId): Result<UserProfile?, AppError> {
        return Result.Ok(mockProfiles[id])
    }

    override suspend fun getAll(): Result<List<UserProfile>, AppError> {
        return Result.Ok(mockProfiles.values.toList())
    }

    override suspend fun add(profile: UserProfile): Result<Unit, AppError> = Result.Ok(Unit)

    override suspend fun update(profile: UserProfile): Result<Unit, AppError> = Result.Ok(Unit)

    override suspend fun addOrUpdate(profile: UserProfile): Result<Unit, AppError> = Result.Ok(Unit)

    override suspend fun remove(id: UserId): Result<Unit, AppError> = Result.Ok(Unit)
}
