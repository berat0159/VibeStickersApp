package com.courage.vibestickers.view.detailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.repository.FakeRepository
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel


@Composable
fun DetailsLazy(
    categoryDetailViewModel: CategoryDetailViewModel,
    onStickerClick: (CategoryDetailStickers) -> Unit
) {

    val categoryDetailStickers = categoryDetailViewModel.categoryDetailStickers.collectAsState()

    val categoryTitle =
        categoryDetailViewModel.categoryDetailStickers.value.firstOrNull()?.categoryTittle ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = categoryTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "${categoryDetailStickers.value.size} Stickerlar",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categoryDetailStickers.value) { categoryDetailStickers ->
                DetailItems(
                    categoryDetailViewModel = categoryDetailViewModel,
                    categoryDetailStickers = categoryDetailStickers,
                    onStickerClick = onStickerClick
                )
            }
        }
    }

}


@Composable
fun DetailItems(
    categoryDetailViewModel: CategoryDetailViewModel,
    categoryDetailStickers: CategoryDetailStickers,
    onStickerClick: (CategoryDetailStickers) -> Unit
) {

    val categoryImage by categoryDetailViewModel.categoryDetailImage.collectAsState()

    val bitmap = categoryImage[categoryDetailStickers.categoryImageUrl]

    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .clickable { onStickerClick(categoryDetailStickers) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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


@Preview(showBackground = true)
@Composable
fun DetailItemPreview() {
    val fakeViewModel = CategoryDetailViewModel(FakeRepository())
    DetailsLazy(categoryDetailViewModel = fakeViewModel) {

    }
}