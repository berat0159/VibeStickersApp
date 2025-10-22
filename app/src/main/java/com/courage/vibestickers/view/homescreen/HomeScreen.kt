package com.courage.vibestickers.view.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.data.model.bannerList
import com.courage.vibestickers.repository.FakeRepository
import com.courage.vibestickers.repository.FakeRepositoryStickersType
import com.courage.vibestickers.repository.FakeRepositoryStory
import com.courage.vibestickers.repository.UserRepository
import com.courage.vibestickers.repository.UserRepositoryImpl
import com.courage.vibestickers.view.homescreen.storytypes.StoryTypes
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.StickersTypeViewModel
import com.courage.vibestickers.viewmodel.StoryTypeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModelStickers: StickersTypeViewModel = hiltViewModel(),
    viewModelStory: StoryTypeViewModel = hiltViewModel(),
    viewModelCategoryDetail: CategoryDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val bannerState = rememberPagerState(initialPage = 0) {
        bannerList.size
    }

    // ViewModel'lerden gelen state'leri burada topla
    val stickersTypeList by viewModelStickers.stickersType.collectAsState()
    val previewImagesMap by viewModelCategoryDetail.categoryStickerOnBoarding.collectAsState()


    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1F1F1))
        , contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                "Keşfet",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp, top = 46.dp)
            )
        }
        item {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StoryTypes(viewModel = viewModelStory, navController = navController)
                HorizontalPager(state = bannerState) { banner ->
                    StickersBanner(banner = bannerList[banner], modifier = modifier)
                }
                Spacer(modifier = modifier.height(3.dp))
                StickerIndicator(
                    modifier = modifier,
                    pageSize = bannerList.size,
                    selectedPage = bannerState.currentPage
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
        items(
            count = stickersTypeList.size,
            key = { index -> stickersTypeList[index].typeId } // Her öğeye benzersiz bir anahtar ver
        ) { index ->
            val type = stickersTypeList[index]

            // StickersTypeLazy içindeki LaunchedEffect mantığını buraya taşı
            LaunchedEffect(type.typeId) {
                if (!previewImagesMap.containsKey(type.typeId)) {
                    viewModelCategoryDetail.fetchCategoryStickerOnBoarding(type.typeId)
                }
            }

            // StickersTypeItem Composable'ını burada çağır
            StickersTypeItem(
                stickersType = type,
                imagesForType = previewImagesMap[type.typeId] ?: emptyList(),
                navController = navController
            )
            // Her item arasına boşluk koymak için
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val auth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
    val userRepository: UserRepository = UserRepositoryImpl(firestore)
    val fakeViewModelStickersType =
        StickersTypeViewModel(FakeRepositoryStickersType(), userRepository, auth)
    val fakeViewModelStory = StoryTypeViewModel(FakeRepositoryStory())
    val navController = rememberNavController()
    HomeScreen(
        modifier = Modifier,
        viewModelStickers = fakeViewModelStickersType,
        viewModelStory = fakeViewModelStory,
        navController = navController
    )
}