package com.empresa.myapplication.Net

import retrofit2.http.GET

// Interfaz de Retrofit para obtener el chiste aleatorio
interface ChuckNorrisApiService {
    @GET("jokes/random") // URL de la API para obtener un chiste aleatorio
    suspend fun getRandomJoke(): ChuckNorrisJoke
}

// Modelo de datos para representar la respuesta del chiste
data class ChuckNorrisJoke(
    val value: String // El campo "value" contiene el chiste
)
