package com.pamn.ggmatch.app.architecture.control.matchPreferences

import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle

interface MatchPreferencesContract {
    interface View {
        fun showState(preferences: Preferences)

        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)

        fun detachView()

        fun load()

        fun toggleRole(role: LolRole)

        fun toggleLanguage(language: Language)

        fun toggleSchedule(schedule: PlaySchedule)

        fun togglePlaystyle(style: Playstyle)

        fun save()
    }
}
