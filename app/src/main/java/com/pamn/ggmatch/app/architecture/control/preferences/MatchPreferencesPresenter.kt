

import com.pamn.ggmatch.app.architecture.control.matchmaking.commands.UpsertMatchPreferencesCommand
import com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers.UpsertMatchPreferencesCommandHandler
import com.pamn.ggmatch.app.architecture.control.preferences.MatchPreferencesContract
import com.pamn.ggmatch.app.architecture.io.matchmaking.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.model.matchmaking.preferences.MatchPreferences
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

    private var currentMatchPreferences: MatchPreferences = MatchPreferences.default()

    private var initialMatchPreferences: MatchPreferences = MatchPreferences.default()

    override fun attachView(view: MatchPreferencesContract.View) {
        this.view = view
        load()
    }

    override fun detachView() {
        this.job.cancel()
        this.view = null
    }

    override fun load() {
        view?.showState(currentMatchPreferences)

        presenterScope.launch {
            when (val result = repository.get(userId)) {
                is Result.Ok -> {
                    val profile = result.value


                    val loadedMatchPreferences =
                        profile?.preferences
                            ?: MatchPreferences.default()

                    initialMatchPreferences = loadedMatchPreferences
                    currentMatchPreferences = loadedMatchPreferences

                    withContext(Dispatchers.Main) {
                        view?.showState(currentMatchPreferences)
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

    private fun updateAndShow(newMatchPreferences: MatchPreferences) {
        currentMatchPreferences = newMatchPreferences
        view?.showState(currentMatchPreferences)
    }

    override fun toggleRole(role: LolRole) {
        val newSet = currentMatchPreferences.roles.toMutableSet()
        if (!newSet.add(role)) newSet.remove(role)
        updateAndShow(currentMatchPreferences.copy(roles = newSet))
    }

    override fun toggleLanguage(language: Language) {
        val newSet = currentMatchPreferences.languages.toMutableSet()
        if (!newSet.add(language)) newSet.remove(language)
        updateAndShow(currentMatchPreferences.copy(languages = newSet))
    }

    override fun toggleSchedule(schedule: PlaySchedule) {
        val newSet = currentMatchPreferences.schedules.toMutableSet()
        if (!newSet.add(schedule)) newSet.remove(schedule)
        updateAndShow(currentMatchPreferences.copy(schedules = newSet))
    }

    override fun togglePlaystyle(style: Playstyle) {
        val newSet = currentMatchPreferences.playstyles.toMutableSet()
        if (!newSet.add(style)) newSet.remove(style)
        updateAndShow(currentMatchPreferences.copy(playstyles = newSet))
    }

    override fun save() {
        if (currentMatchPreferences == initialMatchPreferences) {
            onSaveSuccess()
            return
        }

        val command =
            UpsertMatchPreferencesCommand(
                userId = userId,
                roles = currentMatchPreferences.roles,
                languages = currentMatchPreferences.languages,
                schedules = currentMatchPreferences.schedules,
                playstyles = currentMatchPreferences.playstyles,
            )

        presenterScope.launch {
            val result = upsertMatchPreferencesHandler.handle(command)

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Ok -> {
                        initialMatchPreferences = currentMatchPreferences.copy()
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
