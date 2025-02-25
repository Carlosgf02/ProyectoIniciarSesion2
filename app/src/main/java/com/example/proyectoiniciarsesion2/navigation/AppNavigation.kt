package com.example.proyectoiniciarsesion2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectoiniciarsesion2.ui.screens.*
import com.example.proyectoiniciarsesion2.viewmodel.AuthViewModel
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    carViewModel: CarViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isUserLoggedIn()) "main" else "login"
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
        composable("marcas") {
            MarcasScreen(
                navController = navController,
                viewModel = carViewModel
            )
        }
        composable("modelos") {
            ModelosScreen(
                navController = navController,
                viewModel = carViewModel
            )
        }
    }
} 