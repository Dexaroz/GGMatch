package com.pamn.ggmatch.app.architecture.model.profile

import com.pamn.ggmatch.R

object MockProfileRepository : ProfileRepository {
    private val sampleProfiles =
        listOf(
            Profile(
                id = 1,
                name = "Ahri Main",
                nickname = "FoxfireCharm",
                age = 24,
                description = "Soy main de Mid Lane (Ahri, Syndra). Busco un Jungla sólido para dúo que sepa rotar y gankear post-6.",
                imageRes = R.drawable.profile,
                backgroundImageRes = R.drawable.thresh,
            ),
            Profile(
                id = 2,
                name = "Jungle Diff",
                nickname = "BaronStealer",
                age = 29,
                description = "Main Jungla (Elise, Lee Sin). Me especializo en *invades* agresivos y *early pressure*.",
                imageRes = R.drawable.profile,
                backgroundImageRes = R.drawable.jinx,
            ),
            Profile(
                id = 3,
                name = "Support SoloQ",
                nickname = "PeelGod",
                age = 21,
                description = "Main Soporte *Enchanter* (Lulu, Janna). Soy Plata 1, pero mi *warding* es de Platino.",
                imageRes = R.drawable.profile,
                backgroundImageRes = R.drawable.twisted,
            ),
            Profile(
                id = 4,
                name = "Top Splitpush",
                nickname = "GarenAfk",
                age = 30,
                description = "Top Laner (Garen, Shen). Mi estrategia es el *split push* constante. No me importa el equipo.",
                imageRes = R.drawable.profile,
                backgroundImageRes = R.drawable.jinx,
            ),
            Profile(
                id = 5,
                name = "ADC KDA",
                nickname = "VayneOneTrick",
                age = 26,
                description = "ADC *One Trick* Vayne. Mi KDA es mi vida. Busco un Soporte agresivo (*Engage*) que me dé espacio.",
                imageRes = R.drawable.profile,
                backgroundImageRes = R.drawable.thresh,
            ),
        )

    override fun allProfiles(): List<Profile> {
        return sampleProfiles
    }
}
