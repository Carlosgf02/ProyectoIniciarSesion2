fun forgotPassword(email: String) {
    viewModelScope.launch {
        try {
            _isLoading.value = true
            _error.value = null
            
            if (!isValidEmail(email)) {
                _error.value = "Por favor, introduce un correo electrónico válido"
                return@launch
            }
            
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        if (task.isSuccessful) {
                            _error.value = "Se ha enviado un correo de recuperación a $email"
                        } else {
                            when (task.exception) {
                                is FirebaseAuthInvalidUserException -> 
                                    _error.value = "No existe una cuenta con este correo electrónico"
                                is FirebaseAuthInvalidCredentialsException -> 
                                    _error.value = "El formato del correo electrónico no es válido"
                                else -> 
                                    _error.value = task.exception?.message ?: "Error al enviar el correo de recuperación"
                            }
                        }
                        _isLoading.value = false
                    }
                }
        } catch (e: Exception) {
            _error.value = e.message ?: "Error al enviar el correo de recuperación"
            _isLoading.value = false
        }
    }
} 