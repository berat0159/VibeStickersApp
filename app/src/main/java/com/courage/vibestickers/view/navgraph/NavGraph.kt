package com.courage.vibestickers.view.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.courage.vibestickers.onboarding.OnBoardingScreen
import com.courage.vibestickers.onboarding.OnBoardingViewModel
import com.courage.vibestickers.view.bottomnavigator.Route
import com.courage.vibestickers.view.bottomnavigator.StickerNavigator
import com.courage.vibestickers.view.detailscreen.DetailScreen


@Composable
fun NavGraph(
    startDestination:String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ){
            composable(route = Route.OnBoardingScreen.route){
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(navController = navController, onEvent = viewModel::onEvent)
            }
        }
        navigation(
            route = Route.StickerNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            composable(route = Route.HomeScreen.route) {
                StickerNavigator()
            }
        }

    }
}