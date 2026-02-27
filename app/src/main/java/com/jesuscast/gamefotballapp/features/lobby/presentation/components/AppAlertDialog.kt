package com.jesuscast.gamefotballapp.features.lobby.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/** Type of alert — controls colour, icon and default title. */
enum class AlertType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    NO_CONNECTION
}

private fun AlertType.accentColor(): Color = when (this) {
    AlertType.SUCCESS       -> Color(0xFF00FF00)
    AlertType.ERROR         -> Color(0xFFFF4444)
    AlertType.WARNING       -> Color(0xFFFFAA00)
    AlertType.INFO          -> Color(0xFF4FC3F7)
    AlertType.NO_CONNECTION -> Color(0xFF9E9E9E)
}

private fun AlertType.bgColor(): Color = when (this) {
    AlertType.SUCCESS       -> Color(0xFF001A00)
    AlertType.ERROR         -> Color(0xFF1A0000)
    AlertType.WARNING       -> Color(0xFF1A0F00)
    AlertType.INFO          -> Color(0xFF001020)
    AlertType.NO_CONNECTION -> Color(0xFF111111)
}

private fun AlertType.icon(): ImageVector = when (this) {
    AlertType.SUCCESS       -> Icons.Default.CheckCircle
    AlertType.ERROR         -> Icons.Default.Error
    AlertType.WARNING       -> Icons.Default.Warning
    AlertType.INFO          -> Icons.Default.Info
    AlertType.NO_CONNECTION -> Icons.Default.WifiOff
}

private fun AlertType.defaultTitle(): String = when (this) {
    AlertType.SUCCESS       -> "¡Listo!"
    AlertType.ERROR         -> "Algo salió mal"
    AlertType.WARNING       -> "Atención"
    AlertType.INFO          -> "Información"
    AlertType.NO_CONNECTION -> "Sin conexión"
}

@Composable
fun AppAlertDialog(
    type: AlertType,
    message: String,
    title: String = type.defaultTitle(),
    confirmText: String = "Entendido",
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onConfirm
) {
    val accent = type.accentColor()
    val bg     = type.bgColor()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.92f),
            exit  = fadeOut(tween(180)) + scaleOut(tween(180))
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF0D0D0D))
                    .border(
                        width = 1.dp,
                        color = accent.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(28.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ── Icon badge ────────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(bg)
                            .border(1.5.dp, accent.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = type.icon(),
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    // ── Title ─────────────────────────────────────────────
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // ── Message ───────────────────────────────────────────
                    Text(
                        text = message,
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(4.dp))

                    // ── Buttons ───────────────────────────────────────────
                    if (dismissText != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TextButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = dismissText,
                                    color = Color(0xFF888888),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Button(
                                onClick = onConfirm,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = accent)
                            ) {
                                Text(
                                    text = confirmText,
                                    color = if (type == AlertType.SUCCESS || type == AlertType.WARNING)
                                        Color.Black else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = accent)
                        ) {
                            Text(
                                text = confirmText,
                                color = if (type == AlertType.SUCCESS || type == AlertType.WARNING)
                                    Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

