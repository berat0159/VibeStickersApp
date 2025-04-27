package com.courage.vibestickers.view.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.courage.vibestickers.data.model.StickersBanner

@Composable
fun StickersBanner(
    banner: StickersBanner,
    modifier: Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
            .height(170.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Image(
            painter = painterResource(id = banner.bannerImage),
            contentDescription = banner.bannerTitle,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}



