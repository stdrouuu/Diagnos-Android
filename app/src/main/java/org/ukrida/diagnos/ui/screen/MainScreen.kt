package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.ukrida.diagnos.R
import org.ukrida.diagnos.ui.navigation.BottomNav
import org.ukrida.diagnos.viewmodel.BookingViewModel
import org.ukrida.diagnos.viewmodel.CartViewModel
import org.ukrida.diagnos.viewmodel.HistoryViewModel
import org.ukrida.diagnos.viewmodel.ResultViewModel
import org.ukrida.diagnos.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    role: String,
    navController: NavHostController,
    userViewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val innerNavController = rememberNavController()
    val bookingViewModel = remember { BookingViewModel() }
    val historyViewModel = remember { HistoryViewModel() }
    val resultViewModel = remember { ResultViewModel() }
    val cartViewModel = remember { CartViewModel() }
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()

    val userId = userViewModel.currentUser.value?.id ?: 0
    LaunchedEffect(userId, bookingViewModel.isOrderCompleted) {
        if (userId > 0) {
            historyViewModel.getHistoryList(userId)
        }
    }
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    val showTopBar = currentRoute == "home" || currentRoute == "listtest"

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

                // Right: Cart Icon with Item Badge Counter
                IconButton(
                    onClick = { innerNavController.navigate("cart") },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    BadgedBox(
                        badge = {
                            if (cartViewModel.cartItemCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFE11D48),
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = cartViewModel.cartItemCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Keranjang Saya",
                            tint = Color(0xFF1F2937)
                        )
                    }
                }
                }
            }
        },

        // ================= BOTTOM NAV =================
        bottomBar = {
            if (currentRoute != "history" && !currentRoute.startsWith("result")) {
                BottomNav(innerNavController, role)
            }
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
                    bookingViewModel = bookingViewModel,
                    historyViewModel = historyViewModel,
                    onNavigateToListTest = {
                        innerNavController.navigate("listtest")
                    },
                    onNavigateToDetail = { testId ->
                        innerNavController.navigate("detailtest/$testId")
                    },
                    onNavigateToHistory = {
                        innerNavController.navigate("history")
                    },
                    onNavigateToResult = { bookingId, testId, date ->
                        val dateArg = if (date != null) "?date=${android.net.Uri.encode(date)}" else ""
                        innerNavController.navigate("result/$bookingId/$testId$dateArg")
                    },
                    onNavigateToProfile = {
                        innerNavController.navigate("user")
                    }
                )
            }

            composable("history") {
                HistoryScreen(
                    userId = userId,
                    viewModel = historyViewModel,
                    onBack = {
                        innerNavController.popBackStack()
                    },
                    onNavigateToResult = { bookingId, testId, date ->
                        val dateArg = if (date != null) "?date=${android.net.Uri.encode(date)}" else ""
                        innerNavController.navigate("result/$bookingId/$testId$dateArg")
                    }
                )
            }

            composable(
                route = "result/{bookingId}/{testId}?date={date}",
                arguments = listOf(
                    navArgument("bookingId") { type = NavType.IntType },
                    navArgument("testId") { type = NavType.IntType },
                    navArgument("date") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
                val testId = backStackEntry.arguments?.getInt("testId") ?: 1
                val date = backStackEntry.arguments?.getString("date")
                ResultScreen(
                    bookingId = bookingId,
                    testId = testId,
                    resultViewModel = resultViewModel,
                    bookingViewModel = bookingViewModel,
                    gender = userViewModel.currentUser.value?.gender,
                    date = date,
                    onBack = {
                        innerNavController.popBackStack()
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
                ProfileScreen(
                    viewModel = userViewModel,
                    navController = innerNavController,
                    bookingViewModel = bookingViewModel,
                    historyViewModel = historyViewModel,
                    onNavigateToHistory = {
                        innerNavController.navigate("history")
                    },
                    onLogout = onLogout
                )
            }

            composable("profileedit") {
                ProfileEditScreen(
                    viewModel = userViewModel,
                    navController = innerNavController
                )
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
                    cartViewModel = cartViewModel,
                    onBack = {
                        innerNavController.popBackStack()
                    },
                    onNavigateToReview = {
                        innerNavController.navigate("orderreview")
                    },
                    onNavigateToCart = {
                        innerNavController.navigate("cart")
                    }
                )
            }

            composable("cart") {
                CartScreen(
                    cartViewModel = cartViewModel,
                    userViewModel = userViewModel,
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

            composable("orderreview") {
                OrderReviewScreen(
                    bookingViewModel = bookingViewModel,
                    userViewModel = userViewModel,
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

