package com.example.proyectoiniciarsesion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Recomendación: Usar un sealed class para el estado
sealed class AuthState {
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    object Idle : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado del usuario actual
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    // Estado de carga (isLoading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        _isAuthenticated.value = auth.currentUser != null
    }

    /**
     * Función para actualizar el estado del error.
     */
    fun updateError(message: String?) {
        _error.value = message
    }

    /**
     * Verifica si el usuario está autenticado.
     */
    fun isSignedIn(): Boolean {
        return auth.currentUser != null
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
            _isLoading.emit(true)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al iniciar sesión")
                        }
                    }
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
            try {
                _isLoading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _user.value = auth.currentUser
                            _isAuthenticated.value = true
                            _error.value = null
                        } else {
                            _error.value = task.exception?.localizedMessage ?: "Error desconocido al registrarse"
                        }
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    /**
     * Cierra sesión.
     */
    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _user.emit(null)
            updateError(null) // Limpia cualquier mensaje de error anterior
        }
    }

    /**
     * Inicia sesión de forma anónima.
     */
    fun signInAnonymously() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _user.value = auth.currentUser
                        } else {
                            val errorMessage = when (task.exception?.message) {
                                "This operation is restricted to administrators only." -> 
                                    "El inicio de sesión anónimo no está habilitado. Contacta al administrador."
                                else -> task.exception?.message ?: "Error desconocido"
                            }
                            _error.value = errorMessage
                        }
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
                _isLoading.value = false
            }
        }
    }

    /**
     * Inicia sesión con Google.
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            // Crear credenciales de Google
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            // Iniciar sesión con Firebase
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al iniciar sesión con Google")
                        }
                    }
                }
        }
    }

    /**
     * Envía un correo electrónico de restablecimiento de contraseña al usuario.
     */
    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            updateError("Se ha enviado un correo de restablecimiento de contraseña a $email")
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al enviar el correo de restablecimiento")
                        }
                    }
                }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _user.value = auth.currentUser
                            _isAuthenticated.value = true
                            _error.value = null
                        } else {
                            _error.value = task.exception?.message ?: "Error desconocido"
                        }
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
        _isAuthenticated.value = false
    }

    fun resetPassword(email: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                auth.sendPasswordResetEmail(email).await()
                onComplete(true)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al enviar el correo de recuperación"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}