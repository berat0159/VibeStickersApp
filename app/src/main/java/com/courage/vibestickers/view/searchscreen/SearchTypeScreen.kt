package com.courage.vibestickers.view.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.courage.vibestickers.repository.FakeRepositoryStickersType
import com.courage.vibestickers.repository.FakeRepositoryUser
import com.courage.vibestickers.viewmodel.StickersTypeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun SearchTypeScreen(
    modifier: Modifier = Modifier,
    stickersTypeViewModel: StickersTypeViewModel = hiltViewModel(),
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1F1F1))
    ) {
        SearchTittle()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val queryText = remember { mutableStateOf("") }

            SearchField(query = queryText.value, onQueryChange = {
                queryText.value = it
                stickersTypeViewModel.filterStickerTypes(it)
            })

            // İSTENEN AYIRICI ÇİZGİ
            Text(
                text = "Kategoriler",
                modifier = Modifier.padding(start = 16.dp),
                color = Color.LightGray,
                style = MaterialTheme.typography.titleMedium
            )
            Divider(
                color = Color.LightGray, // Rengi LightGray
                thickness = 1.dp,        // Kalınlığı 1.dp (ince bir çizgi)
                modifier = Modifier
                    .fillMaxWidth()      // Genişliği tam ekran yapsın
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ) // Kenarlardan ve dikeyde biraz boşluk (isteğe bağlı)
            )
            SearchLazy(
                stickersTypeViewModel = stickersTypeViewModel,
                navController = navController
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

    val fakeStickersTypeViewModel =
        StickersTypeViewModel(FakeRepositoryStickersType(), FakeRepositoryUser(), Firebase.auth)
    val navController = rememberNavController()

    SearchTypeScreen(
        modifier = Modifier,
        stickersTypeViewModel = fakeStickersTypeViewModel,
        navController = navController,
    )


}