package com.courage.vibestickers.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.courage.vibestickers.onboarding.Dimens.MediumPadding2
import com.courage.vibestickers.onboarding.Dimens.PageIndicatorWidth
import com.courage.vibestickers.onboarding.components.OnBoardingPage
import com.courage.vibestickers.ui.theme.VibeStickersTheme
import com.courage.vibestickers.view.bottomnavigator.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController : NavController,onEvent: (OnBoardingEvent) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val pageState = rememberPagerState(initialPage = 0) {
            pageArray.size
        }

        val buttonState = remember {
            derivedStateOf {
                when (pageState.currentPage) {
                    0 -> listOf("", "İleri")
                    1 -> listOf("Geri", "İleri")
                    2 -> listOf("Geri", "Başla")
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pageState) { index ->
            OnBoardingPage(page = pageArray[index])
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MediumPadding2)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PageIndicator(
                modifier = Modifier.widthIn(PageIndicatorWidth),
                pageSize = pageArray.size,
                selectedPage = pageState.currentPage,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val scope = rememberCoroutineScope()
                if (buttonState.value[0].isNotEmpty()) {

                    BoardingTextButton(text = buttonState.value[0],
                        onClick = {
                            scope.launch {
                                pageState.animateScrollToPage(
                                    page = pageState.currentPage - 1
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }

                BoardingButton(text = buttonState.value[1], onClick = {
                    scope.launch {
                        if (pageState.currentPage == 2) {
                            onEvent(OnBoardingEvent.SaveAppEntry)
                            navController.navigate(Route.HomeScreen.route)
                        } else {
                            pageState.animateScrollToPage(
                                page = pageState.currentPage + 1
                            )
                        }
                    }
                })
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

