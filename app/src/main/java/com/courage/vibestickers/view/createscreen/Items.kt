package com.courage.vibestickers.view.createscreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import androidx.navigation.NavController
import coil3.Uri
import com.courage.vibestickers.R
import com.courage.vibestickers.viewmodel.CreatedViewModel


@Composable
fun Items(
    selectedImageUri: android.net.Uri?,
    onImageSelected: (android.net.Uri?) -> Unit,
    navController: NavController,
    onCropClick: () -> Unit,
    onAddCropClick: () -> Unit,
) {

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImageSelected(uri)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {

        Image(
            modifier = Modifier
                .size(45.dp)
                .clickable { galleryLauncher.launch("image/*") },
            painter = painterResource(R.drawable.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(40.dp))
        Image(
            modifier = Modifier
                .size(45.dp)
                .clickable { onCropClick()},
            painter = painterResource(R.drawable.crop),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(40.dp))
        Image(
            modifier = Modifier
                .size(45.dp)
                .clickable { },
            painter = painterResource(R.drawable.pencil),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }
    Spacer(modifier = Modifier.height(200.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Cancel",
            modifier = Modifier
                .width(70.dp)
                .clickable { navController.navigate("home_screen") },
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Button(
            modifier = Modifier
                .width(110.dp)
                .height(45.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3B16F),       // Butonun arka planı
                contentColor = Color.White
            ), shape = RoundedCornerShape(16.dp),
            onClick = {
                onAddCropClick()
                navController.popBackStack()
                Toast.makeText(navController.context,"Başarıyla Kaydeildi",Toast.LENGTH_LONG).show()
            }
        ) {
            Text("Kaydet")
        }

    }
}