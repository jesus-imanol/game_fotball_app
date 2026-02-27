package com.jesuscast.gamefotballapp.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.BorderWhite
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.CardGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.NeonGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate500

@Composable
fun ChatInputBar(
    message: String,
    onMessageChanged: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardGreen)
            .border(1.dp, BorderWhite, RoundedCornerShape(24.dp))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Escribe un mensaje...",
                    color = TextSlate500,
                    fontSize = 14.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = NeonGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            singleLine = true,
            enabled = enabled
        )

        IconButton(
            onClick = onSend,
            enabled = true,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (enabled && message.isNotBlank()) NeonGreen else NeonGreen.copy(alpha = 0.3f)
                ),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.Black,
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

