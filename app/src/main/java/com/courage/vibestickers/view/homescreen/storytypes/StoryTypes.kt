package com.courage.vibestickers.view.homescreen.storytypes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.courage.vibestickers.data.model.StoryTypesData
import com.courage.vibestickers.repository.StoryRepositoryImpl
import com.courage.vibestickers.viewmodel.StoryTypeViewModel


@Composable
fun StoryTypes(viewModel: StoryTypeViewModel) {

    val storyTypes by viewModel.storyType.collectAsState()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 45.dp, start = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(storyTypes){ storyTypesData ->
            StoryItems(storyTypesData = storyTypesData, viewModel = viewModel)
        }
    }

}


@Composable
fun StoryItems(storyTypesData: StoryTypesData,viewModel: StoryTypeViewModel) {

    val storyImages by viewModel.storyImage.collectAsState()

    val bitmap = storyImages[storyTypesData.storyImageUrl]


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
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
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = storyTypesData.storyTittle,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewStoryTypes() {
    val viewModel : StoryTypeViewModel = hiltViewModel()
    StoryTypes(viewModel = viewModel)
}