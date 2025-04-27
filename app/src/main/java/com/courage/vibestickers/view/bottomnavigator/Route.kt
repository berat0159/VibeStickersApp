package com.courage.vibestickers.view.bottomnavigator

sealed class Route(
    val route:String
) {
    object OnBoardingScreen:Route("onboarding_screen")
    object HomeScreen:Route("home_screen")
    object CreateScreen:Route("create_screen")
    object BookMarkScreen:Route("bookmark_screen")
    object AccountScreen:Route("account_screen")
    object SearchScreen:Route("search_screen")
    object AppStartNavigation : Route(route = "app_start_navigation")
    object StickerNavigation : Route(route = "sticker_navigation")
    object DetailScreen : Route(route = "detail_screen")
}