package com.pamn.ggmatch.app.architecture.control.matches

import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatchesPresenter(
    private val repository: ProfileRepository,
    private val onBack: () -> Unit,
) : MatchesContract.Presenter {
    private var view: MatchesContract.View? = null

    override fun attachView(view: MatchesContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadProfiles() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = repository.getAll()) {
                is com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok -> {
                    val profiles = result.value
                    CoroutineScope(Dispatchers.Main).launch {
                        view?.showProfiles(profiles)
                    }
                }

                is com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Error -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        view?.showError(result.error.message ?: "Failed to load profiles")
                    }
                }
            }
        }
    }
}
