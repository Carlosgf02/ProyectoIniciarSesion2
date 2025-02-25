package com.example.proyectoiniciarsesion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.proyectoiniciarsesion2.model.Marca
import com.example.proyectoiniciarsesion2.model.Modelo
import com.google.firebase.firestore.Query

class CarViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var userId: String? = null

    private val _marcas = MutableStateFlow<List<Marca>>(emptyList())
    val marcas: StateFlow<List<Marca>> = _marcas

    private val _modelos = MutableStateFlow<List<Modelo>>(emptyList())
    val modelos: StateFlow<List<Modelo>> = _modelos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun initializeData(userId: String) {
        this.userId = userId
        loadMarcas()
        loadModelos()
    }

    fun addMarca(marca: Marca) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val marcaData = hashMapOf(
                        "nombre" to marca.nombre,
                        "año" to marca.año,
                        "pais" to marca.pais,
                        "userId" to currentUser.uid,
                        "createdAt" to System.currentTimeMillis(),
                        "updatedAt" to null
                    )

                    db.collection("marcas")
                        .add(marcaData)
                        .await()

                    loadMarcas()
                }
            } catch (e: Exception) {
                _error.value = "Error al añadir marca: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMarcas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userId?.let { uid ->
                    val snapshot = db.collection("marcas")
                        .whereEqualTo("userId", uid)
                        .get()
                        .await()
                    
                    _marcas.value = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Marca::class.java)?.copy(id = doc.id)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar marcas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMarca(marca: Marca) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val marcaData = hashMapOf(
                    "nombre" to marca.nombre,
                    "año" to marca.año,
                    "pais" to marca.pais,
                    "userId" to marca.userId,
                    "updatedAt" to System.currentTimeMillis()
                )

                db.collection("marcas")
                    .document(marca.id)
                    .update(marcaData as Map<String, Any>)
                    .await()

                loadMarcas()
            } catch (e: Exception) {
                _error.value = "Error al actualizar marca: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMarca(marcaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection("marcas")
                    .document(marcaId)
                    .delete()
                    .await()

                loadMarcas()
            } catch (e: Exception) {
                _error.value = "Error al eliminar marca: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addModelo(modelo: Modelo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val modeloData = hashMapOf(
                        "nombre" to modelo.nombre,
                        "año" to modelo.año,
                        "precio" to modelo.precio,
                        "userId" to currentUser.uid,
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("modelos")
                        .add(modeloData)
                        .await()

                    loadModelos()
                }
            } catch (e: Exception) {
                _error.value = "Error al añadir modelo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadModelos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userId?.let { uid ->
                    val snapshot = db.collection("modelos")
                        .whereEqualTo("userId", uid)
                        .get()
                        .await()
                    
                    _modelos.value = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Modelo::class.java)?.copy(id = doc.id)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar modelos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateModelo(modelo: Modelo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val modeloData = hashMapOf(
                    "nombre" to modelo.nombre,
                    "año" to modelo.año,
                    "precio" to modelo.precio,
                    "userId" to modelo.userId,
                    "updatedAt" to System.currentTimeMillis()
                )

                db.collection("modelos")
                    .document(modelo.id)
                    .set(modeloData)
                    .await()

                loadModelos()
            } catch (e: Exception) {
                _error.value = "Error al actualizar modelo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteModelo(modeloId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection("modelos")
                    .document(modeloId)
                    .delete()
                    .await()

                loadModelos()
            } catch (e: Exception) {
                _error.value = "Error al eliminar modelo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearData() {
        _marcas.value = emptyList()
        _modelos.value = emptyList()
        _error.value = null
        userId = null
    }
} 