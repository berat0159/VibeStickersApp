package com.courage.vibestickers.view.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.courage.vibestickers.data.model.bannerList
import com.courage.vibestickers.repository.StickerRepositoryImpl
import com.courage.vibestickers.repository.StoryRepositoryImpl
import com.courage.vibestickers.view.homescreen.storytypes.StoryTypes
import com.courage.vibestickers.viewmodel.StickersTypeViewModel
import com.courage.vibestickers.viewmodel.StoryTypeViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModelStickers: StickersTypeViewModel = hiltViewModel(),
    viewModelStory: StoryTypeViewModel = hiltViewModel(),
    navController: NavController
) {
    val bannerState = rememberPagerState(initialPage = 0) {
        bannerList.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StoryTypes(viewModel = viewModelStory)
        HorizontalPager(state = bannerState) { banner ->
            StickersBanner(banner = bannerList[banner], modifier = modifier)
        }
        Spacer(modifier = modifier.height(16.dp))
        StickerIndicator(
            modifier = modifier,
            pageSize = bannerList.size,
            selectedPage = bannerState.currentPage
        )
        Spacer(modifier = Modifier.height(16.dp))
        StickersTypeLazy(viewModel = viewModelStickers, navController = navController)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    //HomeScreen()
}