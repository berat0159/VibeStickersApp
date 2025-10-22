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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.courage.vibestickers.ui.theme.VibeStickersTheme
import com.courage.vibestickers.view.navgraph.NavGraph
import com.courage.vibestickers.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()
    @Inject
    lateinit var firebaseAuth:FirebaseAuth
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
                        color = Color(0xFFF1F1F1), // Örnek: Temel renk
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
        signInAnonymouslyIfNeeded()
    }
    private fun signInAnonymouslyIfNeeded() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Giriş başarılı
                        val user = firebaseAuth.currentUser
                        Log.d("Auth", "signInAnonymously:success, UID: ${user?.uid}")
                        // Artık user.uid'yi kullanarak Firestore işlemleri yapabilirsin
                    } else {
                        // Giriş başarısız oldu
                        Log.w("Auth", "signInAnonymously:failure", task.exception)
                        // Hata durumunu yönet (örn: kullanıcıya mesaj göster, tekrar dene)
                    }
                }
        } else {
            // Kullanıcı zaten (anonim veya başka bir yöntemle) giriş yapmış
            Log.d("Auth", "User already signed in: ${firebaseAuth.currentUser?.uid}")
        }
    }
}



