package com.pamn.ggmatch.app.architecture.view.matches.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.BACK_DESCRIPTION
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.MATCHES_TITLE
import com.pamn.ggmatch.app.architecture.view.matches.components.profileCardCompact
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun profileListView(
    profiles: List<UserProfile>,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // --- 1. FONDO COHERENTE ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A))
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.twisted), // Imagen decorativa
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(40.dp)
                .alpha(0.4f)
        )

        // --- 2. CONTENIDO PRINCIPAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.undo),
                    contentDescription = BACK_DESCRIPTION,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() },
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = MATCHES_TITLE,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            // Lista de perfiles
            LazyColumn(
                contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), // Un poco más de espacio entre tarjetas
                modifier = Modifier.fillMaxSize(),
            ) {
                items(profiles) { profile ->
                    // Sugerencia: Envuelve el card en un Box con transparencia si quieres el look Glassmorphism
                    profileCardCompact(profile = profile)
                }
            }
        }
    }
}