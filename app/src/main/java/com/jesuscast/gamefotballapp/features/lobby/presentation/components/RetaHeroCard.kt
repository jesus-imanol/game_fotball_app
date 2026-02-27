package com.jesuscast.gamefotballapp.features.lobby.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Jugador
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta

private const val STADIUM_IMG =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuAqyMvoNyZutiNUhq7LidoJ5te927xqjGrJCHeM9b4WLOlYgdRBYA-TFybPdsDcSMXBWusr0c7W7E0_kQ84Wq8XhLl297Ht5iM2XBtxo7VwkGOqeaHuPHH5QQ7XtGzT_8DObRvUnOXSFuGIe_rR0rCrWjlCJ47JFNo3wUn7ewHM2yB2aCt3oxz-bpY3g6Xo_s0pC3I5tqbkErs-ZJ7AQ9VCfywMU8UwtZ6PbGFWw6RBno4qLwUcv7UO7xChiXLN_Q0ld5_wr_EKp2w"

/**
 * Hero-style Reta card with stadium image background, live badge,
 * neon progress bar, player avatars, and join button.
 * Matches the large card in the design.
 */
@Composable
fun RetaHeroCard(
    reta: Reta,
    isPendingJoin: Boolean,
    currentUserId: String,
    onUnirse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAlreadyJoined = reta.listaJugadores.any { it.usuarioId == currentUserId }
    val isFull = reta.jugadoresActuales >= reta.maxJugadores
    val progress = if (reta.maxJugadores > 0)
        reta.jugadoresActuales.toFloat() / reta.maxJugadores.toFloat() else 0f
    val remaining = reta.maxJugadores - reta.jugadoresActuales

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderWhite, RoundedCornerShape(32.dp))
            .shadow(elevation = 24.dp, shape = RoundedCornerShape(32.dp), clip = false)
    ) {
        Column {
            // ── Stadium image with gradient overlay ───────────────────────
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
            ) {
                AsyncImage(
                    model = STADIUM_IMG,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                // Gradient fade to surface
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.5f to SurfaceDark.copy(alpha = 0.4f),
                                1f to SurfaceDark
                            )
                        )
                )
                // Live badge
                LiveBadge(modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp))
            }

            // ── Content below image (overlaps via negative offset) ────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-64).dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Title + time
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = reta.titulo,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Hoy, ${reta.fechaHora.take(16).takeLast(5)}",
                            color = TextSlate300,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Players + progress panel
                Column(
                    modifier = Modifier
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
                                .shadow(elevation = 0.dp) // glow via tint
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

                // Join button
                Button(
                    onClick = onUnirse,
                    enabled = !isAlreadyJoined && !isFull && !isPendingJoin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAlreadyJoined) Color(0xFF1A3A1A) else NeonGreen,
                        disabledContainerColor = if (isAlreadyJoined) Color(0xFF1A3A1A) else NeonGreen.copy(alpha = 0.3f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    AnimatedVisibility(visible = isPendingJoin, enter = fadeIn(), exit = fadeOut()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.Black
                            )
                            Text(
                                text = "Uniéndose…",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                    }
                    AnimatedVisibility(visible = !isPendingJoin, enter = fadeIn(), exit = fadeOut()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsSoccer,
                                contentDescription = null,
                                tint = if (isAlreadyJoined) NeonGreen else Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = when {
                                    isAlreadyJoined -> "Inscrito ✓"
                                    isFull -> "Reta llena"
                                    else -> "Unirme a la reta"
                                },
                                color = if (isAlreadyJoined) NeonGreen else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Live badge ───────────────────────────────────────────────────────────────

@Composable
private fun LiveBadge(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0.2f, label = "alpha",
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse)
    )

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, NeonGreen.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(NeonGreen.copy(alpha = alpha))
        )
        Text(
            text = "En vivo",
            color = NeonGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─── Overlapping avatar row ───────────────────────────────────────────────────

@Composable
fun OverlappingAvatarRow(
    jugadores: List<Jugador>,
    maxVisible: Int = 6,
    modifier: Modifier = Modifier
) {
    val visible = jugadores.take(maxVisible)
    val overflow = jugadores.size - maxVisible

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-8).dp),
        contentPadding = PaddingValues(end = 4.dp)
    ) {
        items(visible) { jugador ->
            PlayerInitialAvatar(jugador = jugador, borderColor = CardGreen)
        }
        if (overflow > 0) {
            item {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                        .border(2.dp, CardGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$overflow",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─── Single player avatar (initials) ─────────────────────────────────────────

@Composable
fun PlayerInitialAvatar(
    jugador: Jugador,
    borderColor: Color = SurfaceDark,
    modifier: Modifier = Modifier
) {
    val initials = jugador.nombre.split(" ")
        .take(2)
        .joinToString("") { it.take(1).uppercase() }
        .take(2)

    val bgColors = listOf(
        Color(0xFF334155), Color(0xFF1E3A5F), Color(0xFF374151),
        Color(0xFF1F2D3D), Color(0xFF2D3748), Color(0xFF1A202C)
    )
    val bg = bgColors[(jugador.usuarioId.hashCode().and(0x7FFFFFFF)) % bgColors.size]

    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bg)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

