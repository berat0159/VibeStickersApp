package com.courage.vibestickers.view.detailscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.repository.FakeRepository
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.StickersTypeViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DetailScreen(
    categoryDetailViewModel: CategoryDetailViewModel = hiltViewModel(),
    stickersTypeViewModel: StickersTypeViewModel,
    categoryId: String,
    navController: NavController,
){
    val isPackageSheetOpen = remember { mutableStateOf(false) }
    val isStickerSheetOpen = remember { mutableStateOf(false) }
    val isStickerSheetOpenShare = remember { mutableStateOf(false) }
    var selectedSticker by remember { mutableStateOf<CategoryDetailStickers?>(null) }

    // categoryId ilk geldiğinde çalışır
    LaunchedEffect(categoryId) {
        categoryDetailViewModel.fetchCategoryDetailStickers(categoryId)
    }


    Scaffold(
        topBar = { DetailsTopBar { navController.popBackStack() } },
        bottomBar = {
            DetailBottomBar(categoryId = categoryId, stickersTypeViewModel = stickersTypeViewModel, onDownloadClick = {
                isPackageSheetOpen.value = true
            }, onFavoriteClick = {
                stickersTypeViewModel.toggleFavorite(typeId = categoryId)
                Log.d("FAV_CLICK", "Tıklandı: $categoryId")
            }
            )
        },
        backgroundColor = Color(0xFFF1F1F1)
    ) { innerPadding ->
        // İçerik buraya yerleşir, padding alır
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Top ve bottom bar'a göre boşluk verir
        ) {
            DetailsLazy(categoryDetailViewModel = categoryDetailViewModel,
                onStickerClick = { sticker ->
                    selectedSticker = sticker
                    isStickerSheetOpen.value = true
                    }
                )

        }
    }
    // Eğer sheet açılacaksa:
    if (isStickerSheetOpen.value) {

        // sheet açıkken bitmap i gönder
        val bitmap = categoryDetailViewModel.categoryDetailImage.value[selectedSticker!!.categoryImageUrl]
        DetailItemSheet(
            bitmap = bitmap,
            onDismiss = {
                isStickerSheetOpen.value = false

                        },
            onFavoriteClick = { /* favori işlemi */ },
            onDownloadClick = {
                // ilk sheet kapandıktan sonra çağrılacak
                isStickerSheetOpen.value = false
                isStickerSheetOpenShare.value = true

            }
        )
    }

    if (isStickerSheetOpenShare.value && selectedSticker != null) {
        DetailStickerSheet(
            stickerUrl = selectedSticker!!.categoryImageUrl,
            onDismiss = { isStickerSheetOpenShare.value = false }
        )
    }
    if (isPackageSheetOpen.value){
        DetailPackageSheet(
            onDismiss = { isPackageSheetOpen.value = false }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val navController = rememberNavController()
    val fakeViewModel = CategoryDetailViewModel(FakeRepository())

}