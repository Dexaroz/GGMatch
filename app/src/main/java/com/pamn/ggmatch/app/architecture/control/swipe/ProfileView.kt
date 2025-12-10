package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

interface ProfileView {
    fun showProfile(profile: UserProfile) // AHORA RECIBE UserProfile

}