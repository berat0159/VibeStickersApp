package com.courage.vibestickers.view.createscreen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview




@Composable
fun StickerPlaceHolder() {

    Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)){
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val squareSize = size.minDimension / 12 // kare boyutu (12x12 kare gibi)
            val lightGray = Color(0xFFCCCCCC)
            val darkGray = Color(0xFF999999)

            for (y in 0..(size.height / squareSize).toInt()) {
                for (x in 0..(size.width / squareSize).toInt()) {
                    drawRect(
                        color = if ((x + y) % 2 == 0) lightGray else darkGray,
                        topLeft = Offset(x * squareSize, y * squareSize),
                        size = Size(squareSize, squareSize)
                    )
                }
            }

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PlaceHolderPreview() {
    //StickerPlaceHolder()
}