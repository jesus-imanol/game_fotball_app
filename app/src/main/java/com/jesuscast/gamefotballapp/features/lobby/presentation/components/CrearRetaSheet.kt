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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * "Organizar nueva reta" bottom sheet.
 * Matches the dark sheet in the design with:
 *  - Nombre de la reta input
 *  - Hora de inicio input (time picker via text field)
 *  - Capacidad slider (10–22, step 2)
 *  - "Publicar Reta" button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRetaSheet(
    onDismiss: () -> Unit,
    onCrear: (titulo: String, fechaHora: String, maxJugadores: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var titulo by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("20:00") }
    var capacidad by remember { mutableFloatStateOf(14f) }
    var tituloError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF111111),
        tonalElevation = 0.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 48.dp, height = 6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Organizar nueva reta",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // ── Nombre de la reta ─────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "NOMBRE DE LA RETA",
                    color = TextSlate400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                TextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        tituloError = false
                    },
                    placeholder = {
                        Text("Ej. Torneo Relámpago", color = TextSlate600)
                    },
                    isError = tituloError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (tituloError) Color.Red.copy(alpha = 0.6f)
                            else Color.White.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        cursorColor = NeonGreen,
                        focusedLabelColor = NeonGreen,
                    )
                )
                if (tituloError) {
                    Text("El nombre es obligatorio", color = Color.Red, fontSize = 12.sp)
                }
            }

            // ── Hora de inicio ────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "HORA DE INICIO",
                    color = TextSlate400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                TextField(
                    value = hora,
                    onValueChange = { hora = it },
                    placeholder = { Text("HH:MM", color = TextSlate600) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = NeonGreen,
                    )
                )
            }

            // ── Capacidad slider ──────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CAPACIDAD",
                        color = TextSlate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${capacidad.toInt()}",
                            color = NeonGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " jugadores",
                            color = TextSlate500,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                Slider(
                    value = capacidad,
                    onValueChange = { capacidad = it },
                    valueRange = 10f..22f,
                    steps = 5,   // 10,12,14,16,18,20,22  → 5 steps between 7 values
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = NeonGreen,
                        activeTrackColor = NeonGreen,
                        inactiveTrackColor = Color(0xFF334433)
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("10", "16", "22").forEach { mark ->
                        Text(text = mark, color = TextSlate600, fontSize = 10.sp)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Publicar Reta button ──────────────────────────────────────
            Button(
                onClick = {
                    tituloError = titulo.isBlank()
                    if (!tituloError) {
                        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val time = runCatching {
                            LocalTime.parse(hora).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        }.getOrDefault("${hora.take(5)}:00")
                        onCrear(titulo.trim(), "$today $time", capacidad.toInt())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = "Publicar Reta",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}

