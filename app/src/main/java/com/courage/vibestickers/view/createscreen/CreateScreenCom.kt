package com.courage.vibestickers.view.createscreen


import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.courage.vibestickers.viewmodel.CreatedViewModel

@Composable
fun CreateScreenCom(context: Context,navController: NavController, onCropFinished: (Bitmap) -> Unit,createdViewModel: CreatedViewModel = hiltViewModel()) {

    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var isCropMod by remember { mutableStateOf(false) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFF1F1F1)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        StickerPlaceHolder() // placeholder arka plan
        Spacer(modifier = Modifier.height(90.dp))

        Items(
            selectedImageUri = selectedImageUri,
            onImageSelected = { uri -> selectedImageUri = uri },
            navController = navController,
            onCropClick = { isCropMod = true },
            onAddCropClick = {
                createdViewModel.addCreatedStickers(context = context,croppedStickerBitmap = croppedBitmap)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            croppedBitmap != null -> {
                // Crop bittiyse kırpılmış bitmap göster
                Image(
                    bitmap = croppedBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }

            selectedImageUri != null && !isCropMod -> {
                // Normal görünüm
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            selectedImageUri != null && isCropMod -> {
                // Crop modu
                CropScreen(
                    selectedImageUri = selectedImageUri,
                    onCropFinished = { bitmap ->
                        isCropMod = false
                        croppedBitmap = bitmap
                        onCropFinished(bitmap)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateScreenComPreview() {
    val navController = rememberNavController()
    //CreateScreenCom(navController = navController, onCropFinished = {})
}