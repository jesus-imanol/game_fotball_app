package com.jesuscast.gamefotballapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jesuscast.gamefotballapp.core.ui.theme.GamefotballappTheme
import com.jesuscast.gamefotballapp.features.lobby.presentation.LobbyScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamefotballappTheme {
                LobbyScreen()
            }
        }
    }
}
