package com.pamn.ggmatch.app.architecture.control.matchPreferences

import com.pamn.ggmatch.app.architecture.control.matchmaking.commands.UpsertMatchPreferencesCommand
import com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers.UpsertMatchPreferencesCommandHandler
import com.pamn.ggmatch.app.architecture.io.preferences.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchPreferencesPresenter(
    private val userId: UserId,
    private val repository: MatchPreferencesRepository,
    private val upsertMatchPreferencesHandler: UpsertMatchPreferencesCommandHandler,
    private val onSaveSuccess: () -> Unit,
) : MatchPreferencesContract.Presenter {
    private val job = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + job)

    private var view: MatchPreferencesContract.View? = null

    private var currentPreferences: Preferences = Preferences.default()

    private var initialPreferences: Preferences = Preferences.default()

    override fun attachView(view: MatchPreferencesContract.View) {
        this.view = view
        load()
    }

    override fun detachView() {
        this.job.cancel()
        this.view = null
    }

    override fun load() {
        view?.showState(currentPreferences)

        presenterScope.launch {
            when (val result = repository.get(userId)) {
                is Result.Ok -> {
                    val profile = result.value

                    val loadedPreferences =
                        profile?.preferences
                            ?: Preferences.default()

                    initialPreferences = loadedPreferences
                    currentPreferences = loadedPreferences

                    withContext(Dispatchers.Main) {
                        view?.showState(currentPreferences)
                    }
                }
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        view?.showError("Error al cargar preferencias: ${result.error.javaClass.simpleName}")
                    }
                }
            }
        }
    }

    private fun updateAndShow(newPreferences: Preferences) {
        currentPreferences = newPreferences
        view?.showState(currentPreferences)
    }

    override fun toggleRole(role: LolRole) {
        val newSet = currentPreferences.roles.toMutableSet()
        if (!newSet.add(role)) newSet.remove(role)
        updateAndShow(currentPreferences.copy(roles = newSet))
    }

    override fun toggleLanguage(language: Language) {
        val newSet = currentPreferences.languages.toMutableSet()
        if (!newSet.add(language)) newSet.remove(language)
        updateAndShow(currentPreferences.copy(languages = newSet))
    }

    override fun toggleSchedule(schedule: PlaySchedule) {
        val newSet = currentPreferences.schedules.toMutableSet()
        if (!newSet.add(schedule)) newSet.remove(schedule)
        updateAndShow(currentPreferences.copy(schedules = newSet))
    }

    override fun togglePlaystyle(style: Playstyle) {
        val newSet = currentPreferences.playstyles.toMutableSet()
        if (!newSet.add(style)) newSet.remove(style)
        updateAndShow(currentPreferences.copy(playstyles = newSet))
    }

    override fun save() {
        if (currentPreferences == initialPreferences) {
            onSaveSuccess()
            return
        }

        val command =
            UpsertMatchPreferencesCommand(
                userId = userId,
                roles = currentPreferences.roles,
                languages = currentPreferences.languages,
                schedules = currentPreferences.schedules,
                playstyles = currentPreferences.playstyles,
            )

        presenterScope.launch {
            val result = upsertMatchPreferencesHandler.handle(command)

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Ok -> {
                        initialPreferences = currentPreferences.copy()
                        onSaveSuccess()
                    }
                    is Result.Error -> {
                        view?.showError("Error al guardar preferencias: ${result.error.javaClass.simpleName}")
                    }
                }
            }
        }
    }
}
