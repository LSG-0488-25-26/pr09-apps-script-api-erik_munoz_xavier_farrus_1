package com.example.appsscript.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsscript.model.data.Game
import com.example.appsscript.model.repository.GamesRepository
import com.example.appsscript.model.repository.SettingsRepository
import kotlinx.coroutines.launch

class GamesViewModel(
    private val gamesRepository: GamesRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _allGames = MutableLiveData<List<Game>>(emptyList())

    private val _games = MutableLiveData<List<Game>>(emptyList())
    val games: LiveData<List<Game>> = _games

    private val _favoriteGames = MutableLiveData<List<Game>>(emptyList())
    val favoriteGames: LiveData<List<Game>> = _favoriteGames

    private val _favorites = MutableLiveData<Set<String>>(emptySet())
    val favorites: LiveData<Set<String>> = _favorites

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        _favorites.value = settingsRepository.getFavorites()
    }

    fun loadAllGames() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = gamesRepository.getAllGames()
                if (response.status == "ok" && response.data != null) {
                    _allGames.value = response.data
                    applyFilter()
                    updateFavoriteGames()
                } else {
                    _error.value = response.error ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    // Debounce sencillo: si el usuario escribe rápido, solo se filtra tras una pequeña pausa
    // Esto evita que la app crashee por demasiadas búsquedas seguidas
    private var searchJob: kotlinx.coroutines.Job? = null
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            kotlinx.coroutines.delay(300) // 300 ms de espera tras la última tecla
            applyFilter()
        }
    }

    private fun applyFilter() {
        val query = _searchQuery.value.orEmpty().trim()
        val all = _allGames.value.orEmpty()
        _games.value = if (query.isBlank()) all
        else all.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.desarrollador.contains(query, ignoreCase = true) ||
            it.publisher.contains(query, ignoreCase = true)
        }
    }

    fun toggleFavorite(gameId: String) {
        val current = _favorites.value ?: emptySet()
        if (current.contains(gameId)) {
            settingsRepository.removeFavorite(gameId)
        } else {
            settingsRepository.addFavorite(gameId)
        }
        _favorites.value = settingsRepository.getFavorites()
        updateFavoriteGames()
    }

    private fun updateFavoriteGames() {
        val favIds = _favorites.value ?: emptySet()
        _favoriteGames.value = _allGames.value.orEmpty().filter { it.nombre in favIds }
    }

    fun clearError() {
        _error.value = null
    }
}
