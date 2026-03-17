package com.example.appsscript.model.repository

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // --- Usuarios ---

    fun saveUser(username: String, password: String, fullName: String, email: String) {
        val users = getRegisteredUsers().toMutableSet()
        users.add(username)
        prefs.edit()
            .putStringSet("registered_users", users)
            .putString("${username}_password", password)
            .putString("${username}_fullName", fullName)
            .putString("${username}_email", email)
            .apply()
    }

    fun getUserPassword(username: String): String? =
        prefs.getString("${username}_password", null)

    fun isUserRegistered(username: String): Boolean =
        getRegisteredUsers().contains(username)

    private fun getRegisteredUsers(): Set<String> =
        prefs.getStringSet("registered_users", emptySet()) ?: emptySet()

    // --- Favoritos ---

    fun getFavorites(): Set<String> =
        prefs.getStringSet("favorites", emptySet()) ?: emptySet()

    fun addFavorite(gameId: String) {
        val favs = getFavorites().toMutableSet()
        favs.add(gameId)
        prefs.edit().putStringSet("favorites", favs).apply()
    }

    fun removeFavorite(gameId: String) {
        val favs = getFavorites().toMutableSet()
        favs.remove(gameId)
        prefs.edit().putStringSet("favorites", favs).apply()
    }
}
