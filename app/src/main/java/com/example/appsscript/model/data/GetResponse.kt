package com.example.appsscript.model.data

data class GetResponse<T>(
    val status: String,
    val type: String? = null,
    val total: Int? = null,
    val data: T? = null,
    val error: String? = null
)
