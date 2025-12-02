package com.pamn.ggmatch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator
import com.pamn.ggmatch.app.architecture.swipe.view.SwipeView
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeCard
import com.pamn.ggmatch.app.theme.ggMatchTheme
import kotlin.math.roundToInt
import com.pamn.ggmatch.R

class MainActivity : ComponentActivity(), ProfileView {

    private lateinit var presenter: ProfilePresenterImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.init(applicationContext)
        enableEdgeToEdge()

        val profiles = listOf(
            Profile(
                id = 1,
                name = "Laura",
                age = 21,
                description = "Amo los perros",
                imageRes = R.drawable.profile
            ),
            Profile(
                id = 2,
                name = "Carlos",
                age = 22,
                description = "Me gusta viajar",
                imageRes = R.drawable.profile
            ),
            Profile(
                id = 3,
                name = "Marta",
                age = 15,
                description = "Cine",
                imageRes = R.drawable.profile
            )
        )
        val navigator = ProfileNavigator(profiles)

        presenter = ProfilePresenterImplementation(this, navigator)
        presenter.init()

        setContent {
            ggMatchTheme {
                // Aquí se llama a la vista completa de swipe
                SwipeView(presenter = presenter, profiles = profiles)
            }
        }
    }

    override fun showProfile(profile: Profile) {
        // Opcional: si SwipeView necesita notificar cambios de estado
    }
}
