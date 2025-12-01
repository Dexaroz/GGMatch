package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.model.profile.Profile


interface ProfileView {
    fun showProfile(profile: Profile)
}
