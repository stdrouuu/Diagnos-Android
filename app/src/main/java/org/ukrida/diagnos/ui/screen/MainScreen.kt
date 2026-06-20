package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.ukrida.diagnos.ui.navigation.BottomNav
import org.ukrida.diagnos.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    role: String,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val innerNavController = rememberNavController()
    Scaffold(
        // ================= TOP BAR =================
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Diagnos App")
                },
                actions = {
                    // ================= LOGOUT BUTTON =================
                    IconButton(
                        onClick = {
                            // HAPUS DATA LOGIN
                            userViewModel.currentUser.value = null

                            // PINDAH KE LOGIN
                            navController.navigate("login") {

                                // hapus seluruh backstack
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.Red
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        // ================= BOTTOM NAV =================
        bottomBar = {
            BottomNav(innerNavController, role)
        }

    ) { padding ->

        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen()
            }

            composable("product") {
                // ProductScreen(productViewModel)
            }

            if (role == "admin") {

                composable("user") {
                    UserScreen(userViewModel, innerNavController)
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text("Home")
    }
}
