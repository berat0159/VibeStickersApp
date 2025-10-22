package com.courage.vibestickers.view.mystickersscreen

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.courage.vibestickers.R
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.data.model.CreatedStickers
import com.courage.vibestickers.view.detailscreen.CreatedDetailPackageSheet
import com.courage.vibestickers.view.detailscreen.DetailPackageSheet
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.CreatedViewModel



@Composable
fun CreatedDetailScreen(
    createdViewModel: CreatedViewModel = hiltViewModel(),
    navController: NavController,
) {

    val createdStickers2 = createdViewModel.createdDetailStickers.collectAsState()

    val isPackageSheetOpen = remember { mutableStateOf(false) }


    Scaffold(
        topBar = { CreatedDetailTopBar { navController.popBackStack() } },
        bottomBar = {
            CreatedDetailBottomBar {
                isPackageSheetOpen.value = true
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(15.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(createdStickers2.value) { sticker ->
                        CreatedDetailItem(createdViewModel = createdViewModel, createdStickers = sticker, onStickerClick = {})
                    }
                }
            }
        }
    }


    // eğer packed sheet açıksa
    if (isPackageSheetOpen.value) {
        CreatedDetailPackageSheet {
            isPackageSheetOpen.value = false
        }
    }


}

@Composable
fun CreatedDetailBottomBar(
    onDownloadClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    Color(0x94E1D4C9),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onDownloadClick,
                modifier = Modifier
                    .height(45.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                    contentColor = Color.White                // Yazı rengi
                )
            ) {
                androidx.compose.material.Text(text = "Download")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatedDetailTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {},
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )

}

@Composable
fun CreatedDetailItem(
    createdViewModel: CreatedViewModel,
    createdStickers: CreatedStickers,
    onStickerClick: (CategoryDetailStickers) -> Unit
) {

    val createdImage by createdViewModel.createdWillDownloadImages.collectAsState()

    val bitmap = createdImage[createdStickers.createdImageUrl]
    Log.d(" ", "Bitmap value: $bitmap")

    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .clickable {  },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

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



@Preview(showBackground = true)
@Composable
fun CreatedDetailPreview() {
}
