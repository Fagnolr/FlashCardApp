package com.example.navigation.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.nav.R

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val navigationItems = listOf(
        NavigationItem(
            title = stringResource(R.string.appbar_home),
            icon = Icons.Default.Home,
            route = AppRoute.Home,
        ),
        NavigationItem(
            title = stringResource(R.string.appbar_add),
            icon = Icons.Default.Add,
            route = AppRoute.AddFlashCard,
        ),
        NavigationItem(
            title = stringResource(R.string.appbar_list),
            icon = Icons.AutoMirrored.Filled.List,
            route = AppRoute.FlashCardList,
        ),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedItem) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        },
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = Color.Green,
                ),
            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: AppRoute,
)
