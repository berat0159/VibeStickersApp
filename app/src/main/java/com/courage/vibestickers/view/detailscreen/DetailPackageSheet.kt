package com.courage.vibestickers.view.detailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.Bitmap
import com.courage.vibestickers.R
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.CreatedViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPackageSheet(
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val viewmodel: CategoryDetailViewModel = hiltViewModel()
    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.savefile),
                    contentDescription = "Save File",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    text = "İndir",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {

                    viewmodel.makeDownloadStickers(context = context)

                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .height(45.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                    contentColor = Color.White
                )

            ) {
                Text("Cihaza Kaydet")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatedDetailPackageSheet(
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val viewmodel: CreatedViewModel = hiltViewModel()
    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.savefile),
                    contentDescription = "Save File",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    text = "Cihaza Kaydet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {

                    viewmodel.fetchCreatedStickers()
                    viewmodel.makeDownloadCreatedStickers(context = context)

                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .height(45.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                    contentColor = Color.White
                )

            ) {
                Text("Cihaza Kaydet")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailStickerSheet(
    onDismiss: () -> Unit,
    stickerUrl: String
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val viewModel: CategoryDetailViewModel = hiltViewModel()
    val context = LocalContext.current

    // categoryDetailImage map'ini observe et
    val categoryImages = viewModel.categoryDetailImage.collectAsState()

    // URL üzerinden bitmap'i al
    val bitmap = categoryImages.value[stickerUrl]

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.savefile),
                    contentDescription = "Save File",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    text = "Paylaş",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    bitmap?.let {
                        viewModel.shareSticker(context, it)
                    }
                },
                modifier = Modifier
                    .height(45.dp)
                    .width(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                    contentColor = Color.White
                )

            ) {
                Text("Paylaş")
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemSheet(
    bitmap: Bitmap?,
    onDismiss: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDownloadClick: () -> Unit, // Dışarıdan yeni sheet açmak için callback
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Sticker Kendisini Gösterir
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(160.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Satır en alta sabitlensin istiyorsan araya ağırlıklı spacer:
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    var isFavorite by remember { mutableStateOf(false) }

                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick() // veya başka işlem
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Favoriden çıkar" else "Favorilere ekle",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                // 1. sheet animasyonla kapanır
                                sheetState.hide()
                                // sheet state kapandıktan sonra dışarıya bildir

                                // callback ile ikinci sheet açtır
                                onDownloadClick()

                            }
                        },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                            contentColor = Color.White                // Yazı rengi
                        )
                    ) {
                        Text(text = "Download", color = Color.White)
                    }
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailPackageSheetPreview() {
    val isSheetOpenPreview = remember { mutableStateOf(false) }
    if (isSheetOpenPreview.value) {
        DetailPackageSheet(onDismiss = { isSheetOpenPreview.value = false })
    }
}