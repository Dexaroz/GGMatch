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

@Composable
fun profileListView(
    profiles: List<UserProfile>,
    onBack: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.undo),
                // Cambio: BACK_DESCRIPTION
                contentDescription = BACK_DESCRIPTION,
                tint = Color.White,
                modifier =
                    Modifier
                        .size(28.dp)
                        .clickable { onBack() },
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                // Cambio: MATCHES_TITLE
                text = MATCHES_TITLE,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(profiles) { profile ->
                profileCardCompact(profile = profile)
            }
        }
    }
}
