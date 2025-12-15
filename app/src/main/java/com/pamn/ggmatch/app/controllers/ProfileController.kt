package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler

data class ProfileController(
    val upsertUserProfile: UpsertUserProfileCommandHandler,
)
