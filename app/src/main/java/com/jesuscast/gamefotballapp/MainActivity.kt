package com.jesuscast.gamefotballapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jesuscast.gamefotballapp.core.navigation.AppNavigation
import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.core.ui.theme.GamefotballappTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamefotballappTheme {
                AppNavigation(sessionManager = sessionManager)
            }
        }
    }
}
