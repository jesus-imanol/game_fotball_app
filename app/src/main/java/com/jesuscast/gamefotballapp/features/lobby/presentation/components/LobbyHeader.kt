package com.jesuscast.gamefotballapp.features.lobby.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Header: location selector + notification bell + "VolFootball" title + greeting.
 * Matches the sticky dark header in the design.
 */
@Composable
fun LobbyHeader(
    zonaSeleccionada: String,
    zonas: List<String>,
    onZonaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Zone selector + bell ──────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zone pill
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .clickable { expanded = true }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = zonaSeleccionada.replace("_", " ")
                            .replaceFirstChar { it.uppercase() },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextSlate400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    zonas.forEach { zona ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = zona.replace("_", " ")
                                        .replaceFirstChar { it.uppercase() },
                                    color = if (zona == zonaSeleccionada) NeonGreen
                                    else Color.White
                                )
                            },
                            onClick = {
                                expanded = false
                                onZonaSeleccionada(zona)
                            }
                        )
                    }
                }
            }

            // Notification bell
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(SurfaceVariant.copy(alpha = 0.5f))
                    .border(1.dp, BorderWhite, RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificaciones",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                // Neon dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(NeonGreen)
                        .align(Alignment.TopEnd)
                        .padding(end = 8.dp, top = 8.dp)
                )
            }
        }

        // ── Title ─────────────────────────────────────────────────────────
        Text(
            text = "VolFootball",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp
        )
    }
}

