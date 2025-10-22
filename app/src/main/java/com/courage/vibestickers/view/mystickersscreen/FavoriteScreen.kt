package com.courage.vibestickers.view.mystickersscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.view.detailscreen.DetailItems
import com.courage.vibestickers.view.homescreen.StickersTypeItem
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.FavoriteStickersViewModel
import com.courage.vibestickers.viewmodel.StickersTypeViewModel

// FavoriteScreen.kt

@Composable
fun FavoriteScreen(
    favoriteStickersViewModel: FavoriteStickersViewModel, // Bu ViewModel favori detaylarını yönetecek
    stickersTypeViewModel: StickersTypeViewModel,
    categoryDetailViewModel: CategoryDetailViewModel, // Bu ViewModel genel favori ID'lerini ve tüm tipleri tutuyor
    navController: NavController
) {

    // 1. StickersTypeViewModel'den favori olan kategori ID'lerinin Set'ini al
    val favoriteCategoryIdsSet by stickersTypeViewModel.favoriteTypes.collectAsState() // favoriteTypes yerine favoriteTypeIds daha iyi

    val isLoadingFavorites by favoriteStickersViewModel.isLoading.collectAsState()
    // 2. Bu ID listesi değiştiğinde FavoriteStickersViewModel'i tetikle
    LaunchedEffect(favoriteCategoryIdsSet.hashCode()) { // favoriteCategoryIdsSet değiştiğinde bu blok çalışır
        Log.d("FavoriteScreen", "LaunchedEffect triggered. favoriteCategoryIdsSet: $favoriteCategoryIdsSet")
        if (favoriteCategoryIdsSet.isNotEmpty()) {
            Log.d("FavoriteScreen", "Fetching favorite stickers for IDs: $favoriteCategoryIdsSet")
            favoriteStickersViewModel.fetchFavoriteStickers(favoriteCategoryIdsSet.toList())
        } else {
            // Favori ID yoksa, FavoriteStickersViewModel'deki listeyi temizle (veya boş bırak)
            Log.d("FavoriteScreen", "No favorite IDs, clearing favorite stickers list.")
            //favoriteStickersViewModel.clearFavoriteStickers() // Böyle bir fonksiyon eklenebilir
        }
    }
    val favoriteStickerTypesList by favoriteStickersViewModel.favoriteStickers.collectAsState()
    // Resimler için ayrı bir dinleme yapmaya gerek yok, çünkü FavoriteStickerTypeItem içinde
    // favoriteStickersViewModel.favoriteStickerImage zaten kullanılıyor.

    Log.d("FavoriteScreen", "Displaying ${favoriteStickerTypesList.size} favorite sticker types.")
    if (isLoadingFavorites) { // SADECE YÜKLENİYORSA GÖSTER
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Log.d("FavoriteScreen", "Showing loading indicator because isLoadingFavorites is true.")
        }
    } else if (favoriteStickerTypesList.isEmpty() && favoriteCategoryIdsSet.isEmpty()) {
        // Yükleme bitti, hem ID yok hem de liste boş (hiç favori yok)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Henüz favori paketin yok.")
            Log.d("FavoriteScreen", "Showing 'no favorites' message.")
        }
    } else if (favoriteStickerTypesList.isEmpty() && favoriteCategoryIdsSet.isNotEmpty()) {
        // Yükleme bitti, favori ID'leri var ama liste boş (veri bulunamadı veya bir hata oldu)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Favori paketler yüklenemedi veya bulunamadı.")
            Log.d("FavoriteScreen", "Showing 'favorites not found/loaded' message.")
        }
    } else if (favoriteStickerTypesList.isNotEmpty()) {
        // Yükleme bitti ve liste dolu
        Log.d("FavoriteScreen", "Displaying favorite stickers list.")
        StickersTypeLazyList(
            favoriteStickersViewModel = favoriteStickersViewModel,

            categoryDetailViewModel = categoryDetailViewModel,
            // stickersTypeViewModel = stickersTypeViewModel, // Eğer LazyList içinde toggle gerekmiyorsa kaldırılabilir
            navController = navController
        )
    }
}

@Composable
fun StickersTypeLazyList(
    favoriteStickersViewModel: FavoriteStickersViewModel,
    categoryDetailViewModel: CategoryDetailViewModel,
    navController: NavController
) {
    val stickerType = favoriteStickersViewModel.favoriteStickers.collectAsState()
    val stickerOnBoardingImages = categoryDetailViewModel.categoryStickerOnBoarding.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFF1F1F1)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(stickerType.value, key = { it.typeId }) { type ->

            LaunchedEffect(key1 = type.typeId) {
                if (!stickerOnBoardingImages.value.contains(type.typeId)){
                    categoryDetailViewModel.fetchCategoryStickerOnBoarding(type.typeId)
                }
            }
            stickerOnBoardingImages.value[type.typeId]?.let {
                FavoriteStickerTypeItem(
                    stickersType = type,
                    imagesForType = it,
                    navController = navController
                )
            }
        }
    }
}


// FavoriteStickerTypeItem.kt
@Composable
fun FavoriteStickerTypeItem(
    stickersType: StickersType, // Sadece gösterilecek veri
    imagesForType:List<String>,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { navController.navigate("detail_screen/${stickersType.typeId}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stickersType.typeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 17.sp
            )
            Text("${imagesForType.size} Stickers")
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                imagesForType.take(3).forEach { stickerUrl ->
                    AsyncImage(
                        model = stickerUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }

}



