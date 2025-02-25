package com.example.proyectoiniciarsesion2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoiniciarsesion2.ui.screens.LoginScreen
import com.example.proyectoiniciarsesion2.ui.screens.MainScreen
import com.example.proyectoiniciarsesion2.ui.screens.RegisterScreen
import com.example.proyectoiniciarsesion2.ui.screens.ResetPasswordScreen
import com.example.proyectoiniciarsesion2.viewmodel.AuthViewModel
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoiniciarsesion2.ui.screens.MarcasScreen
import com.example.proyectoiniciarsesion2.ui.screens.ModelosScreen
import com.example.proyectoiniciarsesion2.ui.screens.ForgotPasswordScreen
import com.example.proyectoiniciarsesion2.ui.screens.CarApiScreen
import com.example.proyectoiniciarsesion2.viewmodel.CarApiViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    carViewModel: CarViewModel = viewModel()
) {
    val carApiViewModel: CarApiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("main") {
            MainScreen(
                navController = navController,
                authViewModel = authViewModel,
                carViewModel = carViewModel
            )
        }
        composable("marcas_screen") {
            MarcasScreen(
                navController = navController,
                viewModel = carViewModel
            )
        }
        composable("modelos_screen") {
            ModelosScreen(
                navController = navController,
                viewModel = carViewModel
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("car_api") {
            CarApiScreen(
                navController = navController,
                viewModel = carApiViewModel
            )
        }
    }
}
