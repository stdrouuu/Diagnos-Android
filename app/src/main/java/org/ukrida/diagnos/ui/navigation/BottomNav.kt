package org.ukrida.diagnos.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

sealed class Screen(val route: String) {
    object Home : Screen(route = "Home")
    object Product : Screen(route = "Product")
    object User : Screen(route = "User")
}

@Composable
fun BottomNav(navController: NavHostController, role: String) {
    val items = if (role == "admin") {
        listOf(Screen.Home, Screen.Product, Screen.User)
    } else {
        listOf(Screen.Home, Screen.Product)
    }

    NavigationBar {
        items.forEach {
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(it.route) },
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = it.route) },
                label = { Text(it.route) }
            )
        }
    }
}
