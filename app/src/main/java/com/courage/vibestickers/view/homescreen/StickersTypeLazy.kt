package com.courage.vibestickers.view.homescreen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.FakeRepository
import com.courage.vibestickers.repository.FakeRepositoryStickersType
import com.courage.vibestickers.repository.FakeRepositoryUser
import com.courage.vibestickers.ui.theme.VibeStickersTheme
import com.courage.vibestickers.view.bottomnavigator.Route
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.StickersTypeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun StickersTypeLazy(
    stickerTypeViewModel: StickersTypeViewModel,
    categoryDetailViewModel: CategoryDetailViewModel,
    navController: NavController
) {
    val stickersType = stickerTypeViewModel.stickersType.collectAsState()
    val stickerOnBoardingImages by categoryDetailViewModel.categoryStickerOnBoarding.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(stickersType.value, key = {it.typeId}) { type ->
            LaunchedEffect(type.typeId) {
                if (!stickerOnBoardingImages.contains(type.typeId)){
                    categoryDetailViewModel.fetchCategoryStickerOnBoarding(type.typeId)
                }
            }
            StickersTypeItem(
                stickersType = type,
                imagesForType = stickerOnBoardingImages[type.typeId] ?: emptyList(),
                navController = navController
            )
        }
    }
}

@Composable
fun StickersTypeItem(
    stickersType: StickersType,
    imagesForType: List<String>,
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


@Preview(showBackground = true)
@Composable
fun Preview() {
    VibeStickersTheme {
        val firestore = Firebase.auth
        val stickerRepo = FakeRepositoryStickersType()
        val userRepo = FakeRepositoryUser()
        val navController= rememberNavController()
        val categoryDetailViewModel = CategoryDetailViewModel(repository = FakeRepository())
        StickersTypeLazy(stickerTypeViewModel = StickersTypeViewModel(repository = stickerRepo, userRepository = userRepo, firebaseAuth = firestore), navController = navController, categoryDetailViewModel = categoryDetailViewModel)
    }
}