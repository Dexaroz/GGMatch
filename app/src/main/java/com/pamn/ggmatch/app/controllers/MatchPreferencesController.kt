package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers.UpsertMatchPreferencesCommandHandler

data class MatchPreferencesController(
    val upsertMatchPreferences: UpsertMatchPreferencesCommandHandler,
)
