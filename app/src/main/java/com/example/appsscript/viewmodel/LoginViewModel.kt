package com.example.appsscript.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsscript.model.repository.SettingsRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(username: String, password: String) {
        if (username.isBlank()) {
            _errorMessage.value = "El usuario es obligatorio"
            return
        }
        if (password.isBlank()) {
            _errorMessage.value = "La contraseña es obligatoria"
            return
        }
        viewModelScope.launch {
            val storedPwd = settingsRepo.getUserPassword(username)
            if (storedPwd != null && storedPwd == password) {
                _loginSuccess.value = true
                _errorMessage.value = null
            } else {
                _errorMessage.value = "Usuario o contraseña incorrectos"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
