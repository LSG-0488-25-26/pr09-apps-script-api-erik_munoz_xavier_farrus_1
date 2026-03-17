package com.example.appsscript.model.data

data class PostRequest(
    val apiKey: String,
    val nombre: String,
    val desarrollador: String,
    val publisher: String,
    val owners: String,
    val precio: String
)
