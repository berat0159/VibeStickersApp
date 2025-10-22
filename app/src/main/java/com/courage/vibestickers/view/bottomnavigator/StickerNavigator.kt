package com.courage.vibestickers.view.bottomnavigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.courage.vibestickers.R
import com.courage.vibestickers.view.createscreen.CreateScreenCom
import com.courage.vibestickers.view.detailscreen.DetailScreen
import com.courage.vibestickers.view.homescreen.HomeScreen
import com.courage.vibestickers.view.mystickersscreen.CreatedDetailScreen
import com.courage.vibestickers.view.mystickersscreen.OptionsBar
import com.courage.vibestickers.view.profilescreen.ProfileItems
import com.courage.vibestickers.view.profilescreen.ProfileScreen
import com.courage.vibestickers.view.searchscreen.SearchTypeScreen
import com.courage.vibestickers.viewmodel.StickersTypeViewModel


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun StickerNavigator() {

    val navController = rememberNavController()


    val stickersTypeViewModel: StickersTypeViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationIcon(icon = R.drawable.home, label = "Home"),
            BottomNavigationIcon(icon = R.drawable.search, label = "Search"),
            BottomNavigationIcon(icon = R.drawable.mystickers, label = "Favorite"),
            BottomNavigationIcon(icon = R.drawable.user, label = "Account")
        )
    }

    var selectedItem by rememberSaveable { mutableStateOf(0) }

    selectedItem = remember(key1 = navBackStackEntry) {
        when (navBackStackEntry?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.CreateScreen.route -> 2
            Route.BookMarkScreen.route -> 3
            Route.AccountScreen.route -> 4
            else -> 0
        }
    }

    val isBottomBarVisible = remember(key1 = navBackStackEntry) {
        navBackStackEntry?.destination?.route == Route.HomeScreen.route ||
                navBackStackEntry?.destination?.route == Route.BookMarkScreen.route ||
                navBackStackEntry?.destination?.route == Route.AccountScreen.route ||
                navBackStackEntry?.destination?.route == Route.SearchScreen.route
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBar(
                    navController = navController,
                    selected = selectedItem,
                    icons = bottomNavigationItems,
                    onClick = { index ->
                        when (index) {
                            0 -> navController.navigate(Route.HomeScreen.route)
                            1 -> navController.navigate(Route.SearchScreen.route)
                            2 -> navController.navigate(Route.CreateScreen.route)
                            3 -> navController.navigate(Route.BookMarkScreen.route)
                            4 -> navController.navigate(Route.AccountScreen.route)
                        }

                    }
                )
            }

        }
    ) {
        val innerPadding = it

        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.HomeScreen.route) {

                HomeScreen(navController = navController)
            }
            composable(Route.CreateScreen.route) {
                CreateScreenCom(navController = navController,
                    onCropFinished = { croppedBitmap ->
                        // kırpma işlemi bittikten sonra yapılacaklar
                        // örn: kaydet, state’e ata veya başka ekrana gönder
                        println("Crop işlemi tamamlandı -> $croppedBitmap")
                    }
                    , context = context
                )
            }
            composable(Route.BookMarkScreen.route) {
                OptionsBar(navController = navController, stickersTypeViewModel = stickersTypeViewModel)
            }
            composable(Route.AccountScreen.route) {
                ProfileScreen()
            }
            composable(Route.SearchScreen.route) {
                SearchTypeScreen(navController = navController)
            }
            // Detay ekran - parametreli
            composable(
                route = "detail_screen/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""

                DetailScreen(categoryId = categoryId, navController = navController, stickersTypeViewModel = stickersTypeViewModel)
            }
            composable(Route.CreatedDetailScreen.route) {
                CreatedDetailScreen(navController = navController)
            }

        }
    }
}