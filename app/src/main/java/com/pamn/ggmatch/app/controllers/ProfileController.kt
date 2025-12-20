package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.EnsureUserProfileExistsCommandHandler
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.VerifyRiotAccountCommandHandler

data class ProfileController(
    val ensureProfileExists: EnsureUserProfileExistsCommandHandler,
    val upsertUserProfile: UpsertUserProfileCommandHandler,
    val verifyRiotAccount: VerifyRiotAccountCommandHandler,
)
