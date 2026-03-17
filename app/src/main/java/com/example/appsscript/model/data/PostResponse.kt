package com.example.appsscript.model.data

data class PostResponse(
    val status: String,
    val message: String? = null,
    val error: String? = null,
    val fila: Int? = null
)
