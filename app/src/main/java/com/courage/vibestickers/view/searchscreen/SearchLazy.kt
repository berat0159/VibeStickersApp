package com.courage.vibestickers.view.searchscreen

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.FakeRepositoryStickersType
import com.courage.vibestickers.viewmodel.StickersTypeViewModel


@Composable
fun SearchLazy(
    stickersTypeViewModel: StickersTypeViewModel,
    navController: NavController
) {
    val filteredTypes by stickersTypeViewModel.filteredStickersType.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredTypes) { stickerTypeItem ->
            SearchItem(
                stickersType = stickerTypeItem,
                stickersTypeViewModel = stickersTypeViewModel,
                navController = navController,
            )
        }
    }
}


@Composable
fun SearchItem(
    stickersType: StickersType,
    stickersTypeViewModel: StickersTypeViewModel,
    navController: NavController,
) {

    val stickerImage by stickersTypeViewModel.stickerImages.collectAsState()

    val bitmap = stickerImage[stickersType.typeImageUrl]


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {

            navController.navigate("detail_screen/${stickersType.typeId}")
        },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(color = Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stickersType.typeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SearchItemPreview() {
    val stickersType = StickersType()
    //val fakeViewModelType = StickersTypeViewModel(FakeRepositoryStickersType())
    val navController = rememberNavController()
    //SearchItem(stickersType, fakeViewModelType, navController)
}