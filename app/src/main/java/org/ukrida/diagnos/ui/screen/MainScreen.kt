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
import androidx.compose.runtime.*
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.ukrida.diagnos.R
import org.ukrida.diagnos.ui.navigation.BottomNav
import org.ukrida.diagnos.viewmodel.UserViewModel
import org.ukrida.diagnos.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    role: String,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val innerNavController = rememberNavController()
    val bookingViewModel = remember { BookingViewModel() }
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    val showTopBar = currentRoute == "home" || currentRoute == "listtest" || currentRoute == "user"

    Scaffold(
        // ================= TOP BAR =================
        topBar = {
            if (showTopBar) {
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
            modifier = Modifier.padding(
                top = if (showTopBar) padding.calculateTopPadding() else 0.dp,
                bottom = padding.calculateBottomPadding()
            )
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
                ListTestScreen(
                    onNavigateToDetail = { testId ->
                        innerNavController.navigate("detailtest/$testId")
                    }
                )
            }

            composable("user") {
                UserScreen(userViewModel, innerNavController)
            }

            composable(
                route = "detailtest/{testId}",
                arguments = listOf(navArgument("testId") { type = NavType.IntType })
            ) { backStackEntry ->
                val testId = backStackEntry.arguments?.getInt("testId") ?: 1
                DetailTestScreen(
                    testId = testId,
                    bookingViewModel = bookingViewModel,
                    onBack = {
                        innerNavController.popBackStack()
                    },
                    onNavigateToSchedule = {
                        innerNavController.navigate("bookschedule")
                    }
                )
            }

            composable("bookschedule") {
                BookScheduleScreen(
                    bookingViewModel = bookingViewModel,
                    onBack = {
                        innerNavController.popBackStack()
                    },
                    onNavigateToReview = {
                        innerNavController.navigate("orderreview")
                    }
                )
            }

            composable("orderreview") {
                OrderReviewScreen(
                    bookingViewModel = bookingViewModel,
                    onBack = {
                        innerNavController.popBackStack()
                    },
                    onNavigateToProfile = {
                        innerNavController.navigate("user") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}

