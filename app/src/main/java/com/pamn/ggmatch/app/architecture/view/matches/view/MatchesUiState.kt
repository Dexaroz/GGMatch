package com.pamn.ggmatch.app.architecture.view.matches.view

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

sealed interface MatchesUiState {
    object Loading : MatchesUiState

    data class Success(val profiles: List<UserProfile>) : MatchesUiState

    object Empty : MatchesUiState

    data class Error(val message: String) : MatchesUiState
}
