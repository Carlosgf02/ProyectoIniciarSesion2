package com.example.proyectoiniciarsesion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyectoiniciarsesion2.navigation.AppNavigation
import com.example.proyectoiniciarsesion2.ui.theme.ProyectoIniciarSesion2Theme
import com.example.proyectoiniciarsesion2.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel
import com.example.proyectoiniciarsesion2.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoIniciarSesion2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val carViewModel: CarViewModel = viewModel()
                    
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        carViewModel = carViewModel
                    )
                }
            }
        }
    }
}

