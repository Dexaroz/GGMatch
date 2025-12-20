package com.pamn.ggmatch.app.architecture.control.matches

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

interface MatchesContract {
    interface View {
        fun showProfiles(profilesList: List<UserProfile>)

        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)

        fun detachView()

        fun loadProfiles()
    }
}
