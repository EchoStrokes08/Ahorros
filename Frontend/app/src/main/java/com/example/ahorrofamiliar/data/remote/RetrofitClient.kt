package com.example.ahorrofamiliar.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit unico para toda la app.
 *
 * IMPORTANTE: cambia BASE_URL segun tu backend:
 * - Emulador Android: usa 10.0.2.2 en lugar de localhost
 * - Dispositivo fisico: usa la IP de tu PC en la red local
 */
object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.5:3000/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
