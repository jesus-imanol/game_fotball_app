package com.jesuscast.gamefotballapp.features.chat.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesuscast.gamefotballapp.features.chat.presentation.components.ChatInputBar
import com.jesuscast.gamefotballapp.features.chat.presentation.components.ChatMessageItem
import com.jesuscast.gamefotballapp.features.chat.presentation.components.PlayerProgressSection
import com.jesuscast.gamefotballapp.features.chat.presentation.components.RetaDetailHeader
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.BackgroundDark
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetaDetailScreen(
    onBack: () -> Unit,
    viewModel: RetaDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.reta?.titulo ?: "Detalle de reta",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                }

                state.reta == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontró la reta",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }

                else -> {
                    val reta = state.reta!!
                    val isAlreadyJoined = reta.listaJugadores.any {
                        it.usuarioId == state.currentUserId
                    }
                    val isFull = reta.jugadoresActuales >= reta.maxJugadores
                    val isPendingJoin = state.pendingJoinRetaId != null

                    // Scrollable content
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // Header with stadium image
                        item {
                            RetaDetailHeader(reta = reta)
                        }

                        // Player progress section
                        item {
                            PlayerProgressSection(
                                reta = reta,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                )
                            )
                        }

                        // Chat messages
                        if (state.messages.isNotEmpty()) {
                            items(
                                items = state.messages,
                                key = { it.id }
                            ) { message ->
                                ChatMessageItem(
                                    message = message,
                                    isCurrentUser = message.usuarioId == state.currentUserId,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        } else {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Aún no hay mensajes. ¡Sé el primero! 💬",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Bottom spacer
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Chat input bar
                    ChatInputBar(
                        message = state.currentMessage,
                        onMessageChanged = viewModel::onMessageChanged,
                        onSend = viewModel::onSendMessage,
                        enabled = isAlreadyJoined,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Join button
                    if (!isAlreadyJoined) {
                        Button(
                            onClick = viewModel::onUnirse,
                            enabled = !isFull && !isPendingJoin,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                                .navigationBarsPadding()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                disabledContainerColor = NeonGreen.copy(alpha = 0.3f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            AnimatedVisibility(
                                visible = isPendingJoin,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
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
                            AnimatedVisibility(
                                visible = !isPendingJoin,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SportsSoccer,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Text(
                                        text = if (isFull) "Reta llena" else "Unirme a la reta",
                                        color = Color.Black,
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
    }
}

