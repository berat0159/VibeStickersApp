package com.courage.vibestickers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.courage.vibestickers.ui.theme.VibeStickersTheme
import com.courage.vibestickers.view.navgraph.NavGraph
import com.courage.vibestickers.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true) // Varsayılan davranış
        actionBar?.hide()
        setContent {



            val systemUiController = rememberSystemUiController()
            val isDark = isSystemInDarkTheme()

            VibeStickersTheme {

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color(0x1B000000), // Örnek: Temel renk
                        darkIcons = !isDark
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        // Sistem çubukları için boşluk bırakır
                ) {
                    NavGraph(startDestination = viewModel.startDestination.value)
                }

            }
        }
    }
}



