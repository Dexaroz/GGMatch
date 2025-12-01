package com.pamn.ggmatch.app.architecture.control.swipe.commands

import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation

class PreviousProfileCommand(
    private val presenter: ProfilePresenterImplementation
) : Command {

    override fun execute() {
        val newProfile = presenter.navigator().previous()
        presenter.show(newProfile)
    }
}
