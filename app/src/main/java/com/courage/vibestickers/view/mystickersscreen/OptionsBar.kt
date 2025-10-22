package com.courage.vibestickers.view.mystickersscreen

import ads_mobile_sdk.h5
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerDefaults.backgroundColor
import androidx.compose.material.LocalContentColor
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.courage.vibestickers.view.bottomnavigator.Route
import com.courage.vibestickers.view.homescreen.HomeScreen
import com.courage.vibestickers.viewmodel.CategoryDetailViewModel
import com.courage.vibestickers.viewmodel.CreatedViewModel
import com.courage.vibestickers.viewmodel.FavoriteStickersViewModel
import com.courage.vibestickers.viewmodel.StickersTypeViewModel


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun OptionsBar(
    stickersTypeViewModel: StickersTypeViewModel,
    favoriteStickersViewModel: FavoriteStickersViewModel = hiltViewModel(),
    createdViewModel: CreatedViewModel = hiltViewModel(),
    categoryDetailViewModel: CategoryDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val options = listOf("Created by me", "Favorite")

    Column(modifier = Modifier.fillMaxWidth()) {


        // üst başlık
        Text(
            "My Stickers",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        // seçenek barı

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFF3B16F), // istediğin rengi burada belirle
                    height = 3.dp // kalınlığını da değiştirebilirsin
                )

            }
        ) {
            options.forEachIndexed { index, option ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },

                    text = {
                        Text(
                            text = option,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = LocalContentColor.current,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        when (selectedTabIndex) {
            0 -> CreatedAndFavoriteItem(createdViewModel, navController = navController)
            1 -> FavoriteScreen(
                navController = navController,
                favoriteStickersViewModel = favoriteStickersViewModel,
                categoryDetailViewModel = categoryDetailViewModel,
                stickersTypeViewModel = stickersTypeViewModel
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun BarPreview() {
    //OptionsBar()
}