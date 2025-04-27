package com.courage.vibestickers.view.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.ui.theme.VibeStickersTheme

@Composable
    fun DetailBottomBar(
        navController: NavController,
        onDownloadClick: () -> Unit,
        onFavoriteClick: () -> Unit,
    ){


        Box(
            modifier = Modifier.fillMaxWidth().height(60.dp)
                .background(Color.Transparent)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color(0x94E1D4C9), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                var isFavorite by remember { mutableStateOf(false) }

                IconButton(onClick = {
                    isFavorite = !isFavorite
                    onFavoriteClick() // veya başka işlem
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favoriden çıkar" else "Favorilere ekle",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }

                Button(
                    onClick = onDownloadClick,
                    modifier = Modifier
                        .height(45.dp)
                        .width(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEED595),       // Butonun arka planı
                        contentColor = Color.White                // Yazı rengi
                    )
                ) {
                    Text(text = "Download")
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun BottomBarPreview(){
    VibeStickersTheme {
        val navController= rememberNavController()
        DetailBottomBar(navController = navController, onDownloadClick = {}, onFavoriteClick = {})
    }
}