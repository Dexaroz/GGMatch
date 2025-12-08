package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commands.PreviousProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator

class ProfilePresenterImplementation(
    private val view: ProfileView?,
    private val navigator: ProfileNavigator,
) : ProfilePresenter {
    private val nextCommand = NextProfileCommand(this)
    private val previousCommand = PreviousProfileCommand(this)

    private var currentProfile: Profile = navigator.current()

    override fun init() {
        view?.showProfile(currentProfile)
    }

    override fun onNextClicked() {
        nextCommand.execute()
    }

    override fun onPreviousClicked() {
        previousCommand.execute()
    }

    fun navigator(): ProfileNavigator = navigator

    fun show(profile: Profile) {
        currentProfile = profile // 👈 Actualiza el estado del Presenter
        view?.showProfile(currentProfile) // 👈 Notifica a la View (el único punto de salida)
    }
}
