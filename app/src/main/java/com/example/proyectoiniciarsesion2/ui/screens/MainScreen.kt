package com.example.proyectoiniciarsesion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoiniciarsesion2.viewmodel.AuthViewModel
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    carViewModel: CarViewModel
) {
    val isLoading by carViewModel.isLoading.collectAsState()
    val error by carViewModel.error.collectAsState()
    val user by authViewModel.user.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Mantener track de si estamos haciendo logout manualmente
    var isManualSignOut by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        val currentUser = user
        if (currentUser == null && !isManualSignOut) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        } else if (currentUser != null) {
            currentUser.uid?.let { uid ->
                carViewModel.initializeData(uid)
            }
        }
    }

    error?.let {
        LaunchedEffect(it) {
            errorMessage = it
            showErrorDialog = true
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (user?.isAnonymous == true) "Modo Invitado" else "Gestión de Vehículos") },
                actions = {
                    IconButton(
                        onClick = {
                            isManualSignOut = true  // Indicar que es un logout manual
                            carViewModel.clearData()
                            authViewModel.signOut()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (user?.isAnonymous == true) {
                    Text(
                        text = "Modo invitado: Funcionalidad limitada",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Button(
                        onClick = { 
                            isManualSignOut = true  // Indicar que es un logout manual
                            carViewModel.clearData()
                            authViewModel.signOut()
                            navController.navigate("register") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrarse como Usuario")
                    }
                    
                    // Botones limitados para usuarios anónimos
                    Button(
                        onClick = { navController.navigate("marcas_screen") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Marcas (Vista Limitada)")
                    }

                    Button(
                        onClick = { navController.navigate("car_api") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Datos API de Autos")
                    }
                } else {
                    // Interfaz completa para usuarios registrados
                    Button(
                        onClick = { navController.navigate("marcas_screen") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gestionar Marcas")
                    }
                    Button(
                        onClick = { navController.navigate("modelos_screen") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gestionar Modelos")
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    var successMessage = remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val error by authViewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            if (it.contains("Se ha enviado")) {
                successMessage.value = it
                errorMessage.value = ""
            } else {
                errorMessage.value = it
                successMessage.value = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Introduce tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        if (successMessage.value.isNotEmpty()) {
            Text(
                text = successMessage.value,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.value.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.value.isEmpty()) {
                    errorMessage.value = "Por favor, introduce tu correo electrónico"
                    return@Button
                }
                authViewModel.forgotPassword(email.value)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.value.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Enviar correo de recuperación")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { 
                navController.navigate("login") {
                    popUpTo("forgot_password") { inclusive = true }
                }
            }
        ) {
            Text("Volver al inicio de sesión")
        }
    }
}
