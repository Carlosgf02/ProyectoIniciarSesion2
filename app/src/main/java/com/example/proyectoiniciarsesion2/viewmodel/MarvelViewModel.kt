package com.example.proyectoiniciarsesion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoiniciarsesion2.model.Character
import com.example.proyectoiniciarsesion2.network.MarvelApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

class MarvelViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val apiService = Retrofit.Builder()
        .baseUrl(MarvelApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MarvelApiService::class.java)

    fun loadCharacters() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val timestamp = System.currentTimeMillis().toString()
                val hash = generateHash(timestamp)

                val response = apiService.getCharacters(
                    apiKey = MarvelApiService.PUBLIC_KEY,
                    timestamp = timestamp,
                    hash = hash
                )
                
                _characters.value = response.data.results
                if (response.data.results.isEmpty()) {
                    _error.value = "No se encontraron personajes"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar los datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateHash(timestamp: String): String {
        val input = timestamp + MarvelApiService.PRIVATE_KEY + MarvelApiService.PUBLIC_KEY
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
} 