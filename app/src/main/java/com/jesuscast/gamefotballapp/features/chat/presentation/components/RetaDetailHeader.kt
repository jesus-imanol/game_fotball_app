package com.jesuscast.gamefotballapp.features.chat.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.BorderWhite
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.NeonGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.SurfaceDark
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate300

private const val STADIUM_IMG =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuAqyMvoNyZutiNUhq7LidoJ5te927xqjGrJCHeM9b4WLOlYgdRBYA-TFybPdsDcSMXBWusr0c7W7E0_kQ84Wq8XhLl297Ht5iM2XBtxo7VwkGOqeaHuPHH5QQ7XtGzT_8DObRvUnOXSFuGIe_rR0rCrWjlCJ47JFNo3wUn7ewHM2yB2aCt3oxz-bpY3g6Xo_s0pC3I5tqbkErs-ZJ7AQ9VCfywMU8UwtZ6PbGFWw6RBno4qLwUcv7UO7xChiXLN_Q0ld5_wr_EKp2w"

@Composable
fun RetaDetailHeader(
    reta: Reta,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        // Stadium image
        AsyncImage(
            model = STADIUM_IMG,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to SurfaceDark.copy(alpha = 0.4f),
                        1f to SurfaceDark
                    )
                )
        )
        // Live badge
        LiveBadgeSmall(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )
        // Title + time at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = reta.titulo,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Hoy, ${reta.fechaHora.take(16).takeLast(5)}",
                    color = TextSlate300,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LiveBadgeSmall(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, NeonGreen.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(NeonGreen)
        )
        Text(
            text = "En vivo",
            color = NeonGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

