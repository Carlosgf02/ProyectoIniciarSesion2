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
import com.example.proyectoiniciarsesion2.model.Marca
import com.example.proyectoiniciarsesion2.ui.components.MarcaItem
import com.example.proyectoiniciarsesion2.ui.components.AddEditMarcaDialog
import com.example.proyectoiniciarsesion2.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcasScreen(
    navController: NavController,
    viewModel: CarViewModel = viewModel()
) {
    val marcas by viewModel.marcas.collectAsState()
    var selectedMarca by remember { mutableStateOf<Marca?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marcas") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { selectedMarca = Marca() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(marcas) { marca ->
                        MarcaItem(
                            marca = marca,
                            onEdit = { selectedMarca = marca },
                            onDelete = { viewModel.deleteMarca(marca.id) }
                        )
                    }
                }
            }

            selectedMarca?.let { marca ->
                AddEditMarcaDialog(
                    marca = marca,
                    onDismiss = { selectedMarca = null },
                    onSave = { newMarca ->
                        if (marca.id.isEmpty()) {
                            viewModel.addMarca(newMarca)
                        } else {
                            viewModel.updateMarca(newMarca)
                        }
                        selectedMarca = null
                    }
                )
            }
        }
    }
}