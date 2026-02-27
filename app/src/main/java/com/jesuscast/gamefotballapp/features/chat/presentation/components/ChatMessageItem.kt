package com.jesuscast.gamefotballapp.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate400

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        val initials = message.nombreUsuario.split(" ")
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
            .take(2)

        val bgColors = listOf(
            Color(0xFF334155), Color(0xFF1E3A5F), Color(0xFF374151),
            Color(0xFF1F2D3D), Color(0xFF2D3748), Color(0xFF1A202C)
        )
        val bg = bgColors[(message.usuarioId.hashCode().and(0x7FFFFFFF)) % bgColors.size]

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Name + message
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = message.nombreUsuario,
                color = TextSlate400,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.mensaje,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

