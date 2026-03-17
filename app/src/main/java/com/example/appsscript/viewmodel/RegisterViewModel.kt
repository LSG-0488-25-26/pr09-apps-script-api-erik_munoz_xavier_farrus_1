package com.example.appsscript.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsscript.model.repository.SettingsRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _fullNameError = MutableLiveData<String?>()
    val fullNameError: LiveData<String?> = _fullNameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError

    fun register(
        fullName: String,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) {
        var hasError = false

        if (fullName.isBlank()) {
            _fullNameError.value = "El nombre completo es obligatorio"
            hasError = true
        } else {
            _fullNameError.value = null
        }

        if (email.isBlank()) {
            _emailError.value = "El email es obligatorio"
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "El email no es válido"
            hasError = true
        } else {
            _emailError.value = null
        }

        if (username.isBlank()) {
            _usernameError.value = "El nombre de usuario es obligatorio"
            hasError = true
        } else if (settingsRepo.isUserRegistered(username)) {
            _usernameError.value = "Este nombre de usuario ya existe"
            hasError = true
        } else {
            _usernameError.value = null
        }

        if (password.isBlank()) {
            _passwordError.value = "La contraseña es obligatoria"
            hasError = true
        } else if (password.length < 6) {
            _passwordError.value = "Mínimo 6 caracteres"
            hasError = true
        } else {
            _passwordError.value = null
        }

        if (confirmPassword.isBlank()) {
            _confirmPasswordError.value = "Confirma la contraseña"
            hasError = true
        } else if (confirmPassword != password) {
            _confirmPasswordError.value = "Las contraseñas no coinciden"
            hasError = true
        } else {
            _confirmPasswordError.value = null
        }

        if (!hasError) {
            viewModelScope.launch {
                settingsRepo.saveUser(username, password, fullName, email)
                _registerSuccess.value = true
            }
        }
    }
}
