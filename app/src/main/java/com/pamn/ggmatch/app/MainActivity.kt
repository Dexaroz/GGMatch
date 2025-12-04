package com.pamn.ggmatch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.control.swipe.view.swipeView
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator
import com.pamn.ggmatch.app.theme.ggMatchTheme

class MainActivity : ComponentActivity(), ProfileView {
    private lateinit var presenter: ProfilePresenterImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.init(applicationContext)
        enableEdgeToEdge()

        val profiles =
            listOf(
                Profile(
                    id = 1,
                    name = "Bruno Molinari",
                    nickname = "Magalu",
                    age = 21,
                    description = "Amo los perros y el LoL!",
                    imageRes = R.drawable.profile,
                    backgroundImageRes = R.drawable.twisted,
                ),
                Profile(
                    id = 2,
                    name = "Carlos Pérez",
                    nickname = "El Viajero",
                    age = 22,
                    description = "Me gusta viajar y el Genshin Impact.",
                    imageRes = R.drawable.profile,
                    backgroundImageRes = R.drawable.jinx,
                ),
                Profile(
                    id = 3,
                    name = "Marta López",
                    nickname = "Cinéfila",
                    age = 25,
                    description = "Cine y series de fantasía.",
                    imageRes = R.drawable.profile,
                    backgroundImageRes = R.drawable.thresh,
                ),
            )

        val navigator = ProfileNavigator(profiles)

        presenter = ProfilePresenterImplementation(this, navigator)
        presenter.init()

        setContent {
            ggMatchTheme {
                // Aquí se llama a la vista completa de swipe
                swipeView(presenter = presenter, profiles = profiles)
            }
        }
    }

    override fun showProfile(profile: Profile) {
        // Opcional: si SwipeView necesita notificar cambios de estado
    }
}
