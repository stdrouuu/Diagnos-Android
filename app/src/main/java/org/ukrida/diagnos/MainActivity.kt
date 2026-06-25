//usn = brandon, password = 123456
//usn = lebron, password = 123456

package org.ukrida.diagnos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import org.ukrida.diagnos.di.Injection
import org.ukrida.diagnos.ui.screen.LoginScreen
import org.ukrida.diagnos.ui.screen.MainScreen
import org.ukrida.diagnos.ui.screen.RegisterScreen
import org.ukrida.diagnos.ui.screen.WelcomeScreen
import org.ukrida.diagnos.ui.theme.DiagnosTheme
import org.ukrida.diagnos.viewmodel.UserViewModel

class   MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiagnosTheme {
                val navController = rememberNavController()
                val userViewModel = remember { UserViewModel(Injection.userRepo) }

                // State login
                var isLoggedIn by remember { mutableStateOf(false) }
                var role by remember { mutableStateOf("") }

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "main" else "welcome"
                ) {
                    // ================= WELCOME =================
                    composable("welcome") {
                        WelcomeScreen(
                            onNavigateLogin = {
                                navController.navigate("login")
                            }
                        )
                    }
                    // ================= LOGIN =================
                    composable("login") {
                        LoginScreen(
                            viewModel = userViewModel,
                            onLoginSuccess = { userRole ->
                                role = userRole
                                isLoggedIn = true
                                navController.navigate("main") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            },
                            onNavigateRegister = {
                                navController.navigate("register")
                            }
                        )
                    }
                    // ================= REGISTER =================
                    composable("register") {
                        RegisterScreen(
                            viewModel = userViewModel,
                            onRegisterSuccess = {
                                navController.popBackStack()
                            }
                        )
                    }
                    // ================= MAIN =================
                    composable("main") {
                        MainScreen(
                            role = role,
                            navController = navController,
                            userViewModel = userViewModel,
                            onLogout = {
                                isLoggedIn = false
                                role = ""
                                userViewModel.currentUser.value = null
                                navController.navigate("welcome") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
