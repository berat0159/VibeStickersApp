package com.courage.vibestickers.view.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.StickerRepositoryImpl
import com.courage.vibestickers.ui.theme.VibeStickersTheme
import com.courage.vibestickers.viewmodel.StickersTypeViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun StickersTypeLazy(viewModel: StickersTypeViewModel,navController: NavController) {
    val stickersType = viewModel.stickersType.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(stickersType.value) { stickersType ->
            StickersTypeItem(stickersType = stickersType, viewModel = viewModel, navController = navController)
        }
    }
}


@Composable
fun StickersTypeItem(stickersType: StickersType, viewModel: StickersTypeViewModel, navController: NavController) {

    // StateFlow'u State'e çevir
    val stickerImages by viewModel.stickerImages.collectAsState()

    // Resmi Map'ten al
    val bitmap = stickerImages[stickersType.typeImageUrl]


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                navController.navigate("detail_screen/${stickersType.typeId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Yüklenene kadar placeholder göster
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stickersType.typeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    VibeStickersTheme {
        val firestore = Firebase.firestore
        //StickersTypeLazy(viewModel = StickersTypeViewModel(StickerRepositoryImpl(firestore = firestore)))
    }
}