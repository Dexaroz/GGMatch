package com.pamn.ggmatch.app.architecture.control.preferences

import com.pamn.ggmatch.app.architecture.model.profile.preferences.*

class PreferencesPresenter(
    private val initial: Preferences,
    private val onSave: (Preferences) -> Unit
) : PreferencesContract.Presenter {

    private var view: PreferencesContract.View? = null

    private var current = initial.copy()

    override fun attachView(view: PreferencesContract.View) {
        this.view = view
        view.showState(current)
    }

    override fun detachView() {
        this.view = null
    }

    override fun load() {
        view?.showState(current)
    }

    override fun toggleRole(role: LolRole) {
        val newSet = current.favoriteRoles.toMutableSet()

        if (role in newSet) {
            newSet.remove(role)
        } else if (newSet.size < 2) {
            newSet.add(role)
        }

        current = current.copy(favoriteRoles = newSet)
        view?.showState(current)
    }

    override fun toggleLanguage(language: Language) {
        val newSet = current.languages.toMutableSet()
        if (!newSet.add(language)) newSet.remove(language)

        current = current.copy(languages = newSet)
        view?.showState(current)
    }

    override fun toggleSchedule(schedule: PlaySchedule) {
        val newSet = current.playSchedule.toMutableSet()
        if (!newSet.add(schedule)) newSet.remove(schedule)

        current = current.copy(playSchedule = newSet)
        view?.showState(current)
    }

    override fun togglePlaystyle(style: Playstyle) {
        val newSet = current.playstyle.toMutableSet()
        if (!newSet.add(style)) newSet.remove(style)

        current = current.copy(playstyle = newSet)
        view?.showState(current)
    }

    override fun save() {
        onSave(current)
    }
}
