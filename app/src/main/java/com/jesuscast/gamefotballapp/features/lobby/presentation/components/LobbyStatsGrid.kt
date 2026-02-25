package com.jesuscast.gamefotballapp.features.lobby.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 2-column stats grid: "Tus Amigos" + "Ranking".
 * Matches the small cards below the hero card in the design.
 */
@Composable
fun LobbyStatsGrid(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatsCard(
            label = "TUS AMIGOS",
            value = "3",
            subtitle = "Activos",
            icon = Icons.Outlined.Group,
            progressFraction = 0.33f,
            modifier = Modifier.weight(1f)
        )
        StatsCard(
            label = "RANKING",
            value = "#42",
            subtitle = "Global",
            icon = Icons.Outlined.EmojiEvents,
            trendLabel = "Top 5%",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatsCard(
    label: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    progressFraction: Float? = null,
    trendLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(112.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderWhite, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Label + icon row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    color = TextSlate400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextSlate600,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            // Value + subtitle
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "  $subtitle",
                    color = TextSlate400,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Spacer(Modifier.height(6.dp))

            // Progress bar or trend
            if (progressFraction != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(NeonGreen.copy(alpha = 0.5f))
                    )
                }
            }
            if (trendLabel != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "↑", color = NeonGreen, fontSize = 11.sp)
                    Text(
                        text = trendLabel,
                        color = NeonGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

