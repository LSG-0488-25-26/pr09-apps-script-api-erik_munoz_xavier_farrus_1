package com.example.appsscript.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsscript.model.data.PostRequest
import com.example.appsscript.model.repository.GamesRepository
import kotlinx.coroutines.launch
import android.util.Log

class InsertViewModel(private val repository: GamesRepository) : ViewModel() {

    private val _insertSuccess = MutableLiveData<Boolean?>()
    val insertSuccess: LiveData<Boolean?> = _insertSuccess

    private val _insertMessage = MutableLiveData<String?>()
    val insertMessage: LiveData<String?> = _insertMessage

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    // Errores por campo
    private val _nombreError = MutableLiveData<String?>()
    val nombreError: LiveData<String?> = _nombreError

    private val _desarrolladorError = MutableLiveData<String?>()
    val desarrolladorError: LiveData<String?> = _desarrolladorError

    private val _precioError = MutableLiveData<String?>()
    val precioError: LiveData<String?> = _precioError

    fun insertGame(
        nombre: String,
        desarrollador: String,
        publisher: String,
        owners: String,
        precio: String
    ) {
        var hasError = false

        if (nombre.isBlank()) {
            _nombreError.value = "El nombre es obligatorio"
            hasError = true
        } else {
            _nombreError.value = null
        }

        if (desarrollador.isBlank()) {
            _desarrolladorError.value = "El desarrollador es obligatorio"
            hasError = true
        } else {
            _desarrolladorError.value = null
        }

        if (precio.isBlank()) {
            _precioError.value = "El precio es obligatorio"
            hasError = true
        } else {
            _precioError.value = null
        }

        if (hasError) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val request = PostRequest(
                    apiKey = "", // El repositorio lo sobrescribe con la real
                    nombre = nombre,
                    desarrollador = desarrollador,
                    publisher = publisher,
                    owners = owners,
                    precio = precio
                )
                val response = repository.insertGame(request)
                if (response.status == "ok") {
                    _insertSuccess.value = true
                    _insertMessage.value = response.message ?: "Juego insertado correctamente"
                } else {
                    _insertSuccess.value = false
                    _insertMessage.value = response.error ?: "Error al insertar"
                }
            } catch (e: Exception) {
                // MANEJO DEL ERROR 405
                // Google Apps Script tiene un bug conocido: después de procesar
                // correctamente un POST e insertar los datos en el excel,
                // devuelve un error 405 (Method Not Allowed).
                //
                // Hemos verificado que:
                // 1. Los datos si se insertan en Google Sheets (comprobado manualmente)
                // 2. Postman también funciona correctamente
                // 3. El error 405 aparece solo en la respuesta HTTP, pero la operación
                //    fue exitosa en el servidor
                //
                // Por lo tanto, tratamos este error como un caso de éxito
                // para mejorar la experiencia del usuario, que no debe ver un error
                // cuando en realidad todo ha funcionado correctamente.

                if (e.message?.contains("405") == true ||
                    e.message?.contains("Method Not Allowed") == true) {

                    // ✅ Falso positivo: consideramos la operación como exitosa
                    _insertSuccess.value = true
                    _insertMessage.value = "✅ Juego insertado correctamente"


                } else {
                    _insertSuccess.value = false
                    _insertMessage.value = "Error: ${e.message}"

                    // Log del error real
                    Log.e("InsertViewModel", "Error real en insertGame: ${e.message}", e)
                }
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearMessage() {
        _insertMessage.value = null
        _insertSuccess.value = null
    }
}