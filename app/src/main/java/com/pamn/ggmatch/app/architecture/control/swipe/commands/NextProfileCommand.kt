package com.pamn.ggmatch.app.architecture.control.swipe.commands

import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation

class NextProfileCommand(
    private val presenter: ProfilePresenterImplementation,
) : Command {
    override fun execute() {
        val newProfile = presenter.navigator().next()
        presenter.show(newProfile)
    }
}
