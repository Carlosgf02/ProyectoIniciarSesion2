package com.example.proyectoiniciarsesion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.proyectoiniciarsesion2.model.Marca
import com.example.proyectoiniciarsesion2.model.Modelo
import com.example.proyectoiniciarsesion2.repository.CarRepository

class CarViewModel : ViewModel() {
    private val repository = CarRepository()
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
                repository.addMarca(marca).onSuccess {
                    loadMarcas()
                }.onFailure { e ->
                    _error.value = "Error al añadir marca: ${e.message}"
                }
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
                    _marcas.value = repository.getMarcas(uid)
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
                repository.updateMarca(marca).onSuccess {
                    loadMarcas()
                }.onFailure { e ->
                    _error.value = "Error al actualizar marca: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMarca(marcaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteMarca(marcaId).onSuccess {
                    loadMarcas()
                }.onFailure { e ->
                    _error.value = "Error al eliminar marca: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addModelo(modelo: Modelo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.addModelo(modelo).onSuccess {
                    loadModelos()
                }.onFailure { e ->
                    _error.value = "Error al añadir modelo: ${e.message}"
                }
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
                    _modelos.value = repository.getModelos(uid)
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
                repository.updateModelo(modelo).onSuccess {
                    loadModelos()
                }.onFailure { e ->
                    _error.value = "Error al actualizar modelo: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteModelo(modeloId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteModelo(modeloId).onSuccess {
                    loadModelos()
                }.onFailure { e ->
                    _error.value = "Error al eliminar modelo: ${e.message}"
                }
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