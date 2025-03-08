package com.example.proyectoiniciarsesion2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectoiniciarsesion2.model.Modelo
import com.example.proyectoiniciarsesion2.model.Marca

@Composable
fun ModelItem(
    modelo: Modelo,
    marca: Marca? = null,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = modelo.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                marca?.let {
                    Text(
                        text = "Marca: ${it.nombre}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "Año: ${modelo.año}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Precio: $${modelo.precio}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
} 