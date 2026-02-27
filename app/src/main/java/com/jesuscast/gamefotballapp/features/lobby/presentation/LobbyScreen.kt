package com.jesuscast.gamefotballapp.features.lobby.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.AppAlertDialog
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.BackgroundDark
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.CrearRetaSheet
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.LobbyBottomNav
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.LobbyHeader
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.LobbyStatsGrid
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.NeonGreen
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.RetaHeroCard
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.TextSlate400

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            LobbyHeader(
                zonaSeleccionada = state.zonaSeleccionada,
                zonas = state.zonas,
                onZonaSeleccionada = viewModel::onZonaSeleccionada,
                wsConnectionState = state.wsConnectionState
            )
        },
        bottomBar = {
            LobbyBottomNav(modifier = Modifier.navigationBarsPadding())
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onAbrirCrearBottomSheet,
                containerColor = NeonGreen,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear reta",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(paddingValues)
        ) {
            when {
                // ── Loading ───────────────────────────────────────────────
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = NeonGreen,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // ── Empty state ───────────────────────────────────────────
                state.retas.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⚽",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "No hay retas en\n${
                                state.zonaSeleccionada.replace("_", " ")
                                    .replaceFirstChar { it.uppercase() }
                            }",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "¡Crea la primera y convoca al equipo!",
                            color = TextSlate400,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // ── Content list ──────────────────────────────────────────
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Greeting
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Hola, ${state.currentUserNombre.ifBlank { "Campeón" }} 👋",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "¿Listo para la reta de hoy?",
                                    color = TextSlate400,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }

                        // Hero card = first (featured) reta
                        item {
                            RetaHeroCard(
                                reta = state.retas.first(),
                                isPendingJoin = state.pendingJoinRetaId == state.retas.first().id,
                                currentUserId = state.currentUserId,
                                onUnirse = { viewModel.onUnirse(state.retas.first()) }
                            )
                        }

                        // Stats grid below hero
                        item {
                            LobbyStatsGrid()
                        }

                        // Remaining retas as compact hero cards
                        if (state.retas.size > 1) {
                            item {
                                Text(
                                    text = "Más retas disponibles",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            items(
                                items = state.retas.drop(1),
                                key = { it.id }
                            ) { reta ->
                                RetaHeroCard(
                                    reta = reta,
                                    isPendingJoin = state.pendingJoinRetaId == reta.id,
                                    currentUserId = state.currentUserId,
                                    onUnirse = { viewModel.onUnirse(reta) }
                                )
                            }
                        }

                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }

    // ── Create Reta BottomSheet ───────────────────────────────────────────────
    if (state.isCrearBottomSheetVisible) {
        CrearRetaSheet(
            onDismiss = viewModel::onCerrarCrearBottomSheet,
            onCrear = { titulo, fechaHora, maxJugadores, creadorNombre ->
                viewModel.onCrearReta(titulo, fechaHora, maxJugadores, creadorNombre)
            }
        )
    }

    // ── Alert Dialog ─────────────────────────────────────────────────────────
    state.alertEvent?.let { alert ->
        AppAlertDialog(
            type    = alert.type,
            title   = alert.title,
            message = alert.message,
            onConfirm = viewModel::onDismissAlert,
            onDismiss = viewModel::onDismissAlert
        )
    }
}
