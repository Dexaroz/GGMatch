package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

interface ProfilePresenter {
    fun onNextClicked()

    fun onLikeClicked(targetProfile: UserProfile)

    fun onDislikeClicked(targetProfile: UserProfile)

    fun init()
}
