package com.courage.vibestickers.view.bottomnavigator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.courage.vibestickers.R


fun Modifier.topBorder(strokeWidth: Dp, color: Color) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        drawLine(
            color = color,
            start = Offset(0f, 0f), // Sol üst
            end = Offset(size.width, 0f), // Sağ üst
            strokeWidth = strokeWidthPx
        )
    }
)
@Composable
fun BottomBar(
    navController: NavHostController,
    selected: Int,
    onClick: (Int) -> Unit,
    icons: List<BottomNavigationIcon>
) {
    val colorScheme = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        // Ana Navigation Bar
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp).topBorder(1.dp, color = Color.DarkGray),
            containerColor = Color.White,
            contentColor = Color.White,
            tonalElevation = 25.dp
        ) {
            // Sol taraftaki 2 ikon (Home ve Search)
            icons.take(2).forEachIndexed { index, icon ->
                NavigationBarItem(
                    selected = selected == index,
                    onClick = { onClick(index) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = icon.icon),
                            contentDescription = icon.label,
                            tint = if (selected == index) Color(0xFF000000) else Color(0x56000000)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF000000),
                        unselectedIconColor = Color(0x56000000),
                        indicatorColor = Color.Transparent
                    )
                )
            }

            // Ortada boş bir item (FAB için yer açmak amacıyla)
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Box(modifier = Modifier.size(24.dp)) },
                enabled = false
            )

            // Sağ taraftaki 2 ikon (Extensions ve Profile)
            icons.drop(2).forEachIndexed { index, icon ->
                NavigationBarItem(
                    selected = selected == index + 3, // İlk 2 item sol tarafta olduğu için +2
                    onClick = { onClick(index + 3) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = icon.icon),
                            contentDescription = icon.label,
                            tint = if (selected == index + 3) Color(0xFF000000) else Color(0x56000000)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorScheme.primary,
                        unselectedIconColor = colorScheme.onSurfaceVariant,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }

        FloatingActionButton(
            onClick = { onClick(2) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp)
                .size(60.dp),
            backgroundColor = Color(0xFFFF751F),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

data class BottomNavigationIcon(
    val icon: Int,
    val label: String
)


@Preview(showBackground = true)
@Composable
fun BottomBarPreview(){
    val navController = rememberNavController()
    val iconList = listOf(
        BottomNavigationIcon(R.drawable.home,"home"),
        BottomNavigationIcon(R.drawable.search, "Search"),
        BottomNavigationIcon(R.drawable.mystickers, "Favorites"),
        BottomNavigationIcon(R.drawable.user, "Account")
    )
    BottomBar(navController = navController, selected = 0, onClick = {}, icons = iconList)
}
