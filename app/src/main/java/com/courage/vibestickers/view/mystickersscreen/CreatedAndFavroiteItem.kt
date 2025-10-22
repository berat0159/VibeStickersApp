package com.courage.vibestickers.view.mystickersscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.courage.vibestickers.view.bottomnavigator.Route
import com.courage.vibestickers.viewmodel.CreatedViewModel


@Composable
fun CreatedAndFavoriteItem(createdViewModel: CreatedViewModel, navController: NavController) {

    val createdStickers by createdViewModel.createdStickerImages.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clickable { navController.navigate(Route.CreatedDetailScreen.route) },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {


        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Oluşturlanlar",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp, top = 15.dp)
            )
            Text(
                "${createdStickers.size} Sticker",
                color = Color.LightGray,
                modifier = Modifier.padding(start = 15.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
                ) {

                    createdStickers.take(3).forEach { sticker ->
                        AsyncImage(
                            model = sticker,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }

    }

}
