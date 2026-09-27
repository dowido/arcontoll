package com.example.doncontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doncontrol.ui.screens.ControlShadeScreen
import com.example.doncontrol.ui.screens.TileCustomizerScreen
import com.example.doncontrol.ui.theme.BackgroundDark
import com.example.doncontrol.ui.theme.DoncontrolTheme
import com.example.doncontrol.viewmodel.ControlCenterViewModel
import com.example.doncontrol.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoncontrolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    MainControlApp()
                }
            }
        }
    }
}

@Composable
fun MainControlApp(viewModel: ControlCenterViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            ScreenState.CONTROL_SHADE -> {
                ControlShadeScreen(
                    viewModel = viewModel,
                    onNavigateToCustomizer = { viewModel.navigateTo(ScreenState.TILE_CUSTOMIZER) }
                )
            }
            ScreenState.TILE_CUSTOMIZER -> {
                TileCustomizerScreen(
                    viewModel = viewModel,
                    onDoneClick = { viewModel.navigateTo(ScreenState.CONTROL_SHADE) }
                )
            }
        }
    }
}