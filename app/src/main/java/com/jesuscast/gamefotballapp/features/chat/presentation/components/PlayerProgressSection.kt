package com.jesuscast.gamefotballapp.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.BorderWhite
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.CardGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.NeonGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.OverlappingAvatarRow
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate300
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate400

@Composable
fun PlayerProgressSection(
    reta: Reta,
    modifier: Modifier = Modifier
) {
    val progress = if (reta.maxJugadores > 0)
        reta.jugadoresActuales.toFloat() / reta.maxJugadores.toFloat() else 0f
    val remaining = reta.maxJugadores - reta.jugadoresActuales
    val isFull = reta.jugadoresActuales >= reta.maxJugadores

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardGreen)
            .border(1.dp, BorderWhite, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Count row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jugadores confirmados",
                color = TextSlate300,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row {
                Text(
                    text = "${reta.jugadoresActuales}",
                    color = NeonGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " / ${reta.maxJugadores}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonGreen)
            )
        }

        // "Faltan X jugadores"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(NeonGreen)
            )
            Text(
                text = if (isFull) "¡Reta completa!"
                else "Faltan $remaining jugadores para completar",
                color = TextSlate400,
                fontSize = 12.sp
            )
        }

        // Overlapping avatar row
        if (reta.listaJugadores.isNotEmpty()) {
            OverlappingAvatarRow(
                jugadores = reta.listaJugadores,
                maxVisible = 6
            )
        }
    }
}

