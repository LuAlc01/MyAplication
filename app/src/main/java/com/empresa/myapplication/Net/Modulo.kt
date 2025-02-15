package com.empresa.myapplication.Net

// Importamos las librerías necesarias para la configuración de la red usando Retrofit y Gson.
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Este módulo de Dagger proporciona las dependencias necesarias para la configuración de la red.
// La anotación @Module indica que esta clase es un módulo de Dagger y proporciona objetos a través de @Provides.
// La anotación @InstallIn(SingletonComponent::class) indica que las dependencias serán de ámbito Singleton en la aplicación.
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Proporciona una instancia de Gson para convertir objetos de JSON a Kotlin y viceversa.
    // Gson se usa para procesar el JSON que obtenemos de la API.
    @Provides
    @Singleton // Asegura que solo haya una instancia de Gson en la aplicación.
    fun provideGson(): Gson = GsonBuilder().create()

    // Proporciona una instancia de Retrofit configurada con la base URL y el convertidor Gson.
    // Retrofit es una librería que facilita la creación de peticiones HTTP y la conversión de respuestas JSON.
    @Provides
    @Singleton // Solo se crea una única instancia de Retrofit en toda la aplicación.
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/") // URL base de la API de Chuck Norris.
            .addConverterFactory(GsonConverterFactory.create(gson)) // Usamos Gson como convertidor de JSON.
            .build() // Construimos la instancia de Retrofit con la configuración dada.
    }

    // Proporciona la instancia del servicio de la API de Chuck Norris utilizando Retrofit.
    // Esta función crea una interfaz que permite hacer las peticiones HTTP a la API.
    @Provides
    @Singleton // Solo se crea una instancia de ChuckNorrisApiService en la aplicación.
    fun provideChuckNorrisApiService(retrofit: Retrofit): ChuckNorrisApiService {
        // Creamos el servicio a partir de Retrofit, que se encarga de realizar las peticiones HTTP.
        return retrofit.create(ChuckNorrisApiService::class.java)
    }
}
