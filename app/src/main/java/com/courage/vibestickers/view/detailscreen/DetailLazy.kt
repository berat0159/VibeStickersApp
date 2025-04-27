package com.courage.vibestickers.view.detailscreen

import android.telecom.Call.Details
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel


@Composable
fun DetailsLazy(categoryDetailViewModel: CategoryDetailViewModel) {

    val categoryDetailStickers = categoryDetailViewModel.categoryDetailStickers.collectAsState()

    val categoryTitle = categoryDetailViewModel.categoryDetailStickers.value.firstOrNull()?.categoryTittle ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ){
        Text(
            text = categoryTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(15.dp)
        ) {
            items(categoryDetailStickers.value) { categoryDetailStickers ->
                DetailItems(
                    categoryDetailViewModel = categoryDetailViewModel,
                    categoryDetailStickers = categoryDetailStickers
                )
            }
        }
    }

}


@Composable
fun DetailItems(
    categoryDetailViewModel: CategoryDetailViewModel,
    categoryDetailStickers: CategoryDetailStickers
) {

    val categoryImage by categoryDetailViewModel.categoryDetailImage.collectAsState()

    val bitmap = categoryImage[categoryDetailStickers.stickerImageUrl]

    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(85.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            } else {
                // Yüklenene kadar placeholder göster
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray)
                )
            }
        }


    }
}