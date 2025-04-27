package com.courage.vibestickers.view.detailscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel


@Composable
fun DetailScreen(
    categoryDetailViewModel: CategoryDetailViewModel = hiltViewModel(),
    categoryId: String,
    navController: NavController
){

    // categoryId ilk geldiğinde çalışır
    LaunchedEffect(categoryId) {
        categoryDetailViewModel.fetchCategoryDetailStickers(categoryId)
    }


    Scaffold(
        topBar = { DetailsTopBar { navController.popBackStack() } },
        bottomBar = {
            DetailBottomBar(navController = navController, onDownloadClick = { /*TODO*/ }, onFavoriteClick = { /*TODO*/ })
        }
    ) { innerPadding ->
        // İçerik buraya yerleşir, padding alır
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 👈 Top ve bottom bar'a göre boşluk verir
        ) {
            DetailsLazy(categoryDetailViewModel = categoryDetailViewModel)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val navController = rememberNavController()
    DetailScreen(categoryId = "funny", navController = navController)
}