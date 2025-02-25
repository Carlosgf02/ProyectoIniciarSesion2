package com.example.proyectoiniciarsesion2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoiniciarsesion2.model.Modelo
import com.example.proyectoiniciarsesion2.ui.components.ModelItem
import com.example.proyectoiniciarsesion2.ui.components.AddEditModelDialog
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelosScreen(
    navController: NavController,
    viewModel: CarViewModel
) {
    val modelos by viewModel.modelos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedModelo by remember { mutableStateOf<Modelo?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadModelos()  // Cargar modelos al entrar a la pantalla
    }

    // Mostrar SnackBar con errores
    error?.let {
        LaunchedEffect(it) {
            // Puedes usar un Scaffold con SnackBar para mostrar el error
            println("Error en ModelosScreen: $it")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modelos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { selectedModelo = Modelo() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(modelos) { modelo ->
                ModelItem(
                    modelo = modelo,
                    onEdit = { selectedModelo = modelo },
                    onDelete = { viewModel.deleteModelo(modelo.id) }
                )
            }
        }

        selectedModelo?.let { modelo ->
            AddEditModelDialog(
                modelo = modelo,
                onDismiss = { selectedModelo = null },
                onSave = { newModelo ->
                    if (modelo.id.isEmpty()) {
                        viewModel.addModelo(newModelo)
                    } else {
                        viewModel.updateModelo(newModelo)
                    }
                    selectedModelo = null
                }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
} 