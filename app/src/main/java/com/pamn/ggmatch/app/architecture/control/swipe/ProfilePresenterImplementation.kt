package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commands.PreviousProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator

class ProfilePresenterImplementation(
    private val view: ProfileView,
    private val navigator: ProfileNavigator,
) : ProfilePresenter {
    private val nextCommand = NextProfileCommand(this)
    private val previousCommand = PreviousProfileCommand(this)

    override fun init() {
        view.showProfile(navigator.current())
    }

    override fun onNextClicked() {
        nextCommand.execute()
    }

    override fun onPreviousClicked() {
        previousCommand.execute()
    }

    // Métodos internos que usan los commands
    fun navigator(): ProfileNavigator = navigator

    fun show(profile: Profile) {
        view.showProfile(profile)
    }
}
