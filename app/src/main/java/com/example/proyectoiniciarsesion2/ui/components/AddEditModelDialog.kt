package com.example.proyectoiniciarsesion2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.proyectoiniciarsesion2.model.Modelo
import com.example.proyectoiniciarsesion2.model.Marca

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditModelDialog(
    modelo: Modelo?,
    marcas: List<Marca>,
    onDismiss: () -> Unit,
    onSave: (Modelo) -> Unit
) {
    var nombre by remember { mutableStateOf(modelo?.nombre ?: "") }
    var año by remember { mutableStateOf(modelo?.año?.toString() ?: "") }
    var precio by remember { mutableStateOf(modelo?.precio?.toString() ?: "") }
    var selectedMarcaId by remember { mutableStateOf(modelo?.marcaId ?: "") }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (modelo?.id?.isEmpty() != false) "Añadir Modelo" else "Editar Modelo",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Dropdown para seleccionar marca
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = marcas.find { it.id == selectedMarcaId }?.nombre ?: "Selecciona una marca",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Marca") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        marcas.forEach { marca ->
                            DropdownMenuItem(
                                text = { Text(marca.nombre) },
                                onClick = {
                                    selectedMarcaId = marca.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = año,
                    onValueChange = { año = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            if (selectedMarcaId.isNotEmpty()) {
                                val newModelo = Modelo(
                                    id = modelo?.id ?: "",
                                    nombre = nombre,
                                    año = año.toIntOrNull() ?: 0,
                                    precio = precio.toFloatOrNull() ?: 0f,
                                    userId = modelo?.userId ?: "",
                                    marcaId = selectedMarcaId
                                )
                                onSave(newModelo)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        enabled = selectedMarcaId.isNotEmpty()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
} 