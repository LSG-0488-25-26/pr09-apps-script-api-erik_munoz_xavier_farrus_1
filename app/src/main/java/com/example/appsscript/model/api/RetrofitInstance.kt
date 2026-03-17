package com.example.appsscript.model.api

import com.example.appsscript.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Google Apps Script devuelve una redirección 302 en las peticiones POST.
    // OkHttp por defecto convierte POST a GET al seguir redirects (RFC).
    // Este interceptor sigue los redirects manteniendo el método original.
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .addInterceptor { chain ->
            var response = chain.proceed(chain.request())
            var redirectCount = 0
            while (response.code in 301..308 && redirectCount < 5) {
                val location = response.header("Location") ?: break
                response.close()
                val newRequest = chain.request().newBuilder().url(location).build()
                response = chain.proceed(newRequest)
                redirectCount++
            }
            response
        }
        .build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
