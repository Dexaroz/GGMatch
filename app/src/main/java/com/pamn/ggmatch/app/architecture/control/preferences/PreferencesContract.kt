package com.pamn.ggmatch.app.architecture.control.preferences

import com.pamn.ggmatch.app.architecture.model.profile.preferences.*

interface PreferencesContract {

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
