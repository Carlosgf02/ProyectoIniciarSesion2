package com.example.proyectoiniciarsesion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado del usuario actual
    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Función para actualizar el estado del error.
     */
    fun updateError(message: String?) {
        _error.value = message
    }

    /**
     * Inicia sesión con email y password.
     */
    fun login(email: String, password: String) {
        if (!isValidEmail(email)) {
            updateError("Email inválido")
            return
        }
        if (password.length < 6) {
            updateError("La contraseña debe tener al menos 6 caracteres")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido al iniciar sesión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Registra un usuario nuevo con email y password.
     */
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido al registrarse"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cierra sesión.
     */
    fun signOut() {
        auth.signOut()
        _user.value = null
        _error.value = null
    }

    /**
     * Inicia sesión de forma anónima.
     */
    fun signInAnonymously() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInAnonymously().await()
                _user.value = auth.currentUser
                _error.value = null
            } catch (e: Exception) {
                val errorMessage = when (e.message) {
                    "This operation is restricted to administrators only." -> 
                        "El inicio de sesión anónimo no está habilitado. Contacta al administrador."
                    else -> e.message ?: "Error desconocido"
                }
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Inicia sesión con Google.
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _user.value = auth.currentUser
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido al iniciar sesión con Google"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Envía un correo electrónico de restablecimiento de contraseña al usuario.
     */
    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.sendPasswordResetEmail(email).await()
                _error.value = "Se ha enviado un correo de restablecimiento de contraseña a $email"
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido al enviar el correo de restablecimiento"
            } finally {
                _isLoading.value = false
            }
        }
    }
}