package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.ukrida.diagnos.R
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Left: Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp),
                    contentScale = ContentScale.Fit
                )

                // Center: Logo name
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logoname),
                        contentDescription = "Diagnos Logo Name",
                        modifier = Modifier.height(28.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
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
                HomeScreen(
                    userViewModel = userViewModel,
                    onNavigateToListTest = {
                        innerNavController.navigate("listtest")
                    }
                )
            }

            composable("listtest") {
                ListTestScreen()
            }

            composable("user") {
                UserScreen(userViewModel, innerNavController)
            }
        }
    }
}

