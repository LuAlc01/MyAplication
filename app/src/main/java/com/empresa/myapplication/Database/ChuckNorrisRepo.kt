package com.empresa.myapplication.Database

import com.empresa.myapplication.Net.ChuckNorrisApiService
import javax.inject.Inject

/**
 * Repositorio que se encarga de interactuar con la API de Chuck Norris para obtener datos.
 * Este repositorio actúa como una capa intermedia entre la API y el resto de la aplicación,
 * proporcionando una abstracción sobre la fuente de datos.
 *
 * @property chuckNorrisApiService El servicio de la API de Chuck Norris, inyectado por Hilt.
 */
class ChuckNorrisRepository @Inject constructor(
    private val chuckNorrisApiService: ChuckNorrisApiService // Inyección de dependencias del servicio de la API
) {
    /**
     * Obtiene un chiste aleatorio desde la API de Chuck Norris.
     * Esta funcion realiza una llamada a la API y devuelve el chiste obtenido.
     *
     * @return Un `String` que contiene el chiste aleatorio.
     */
    suspend fun getRandomJoke(): String {
        return chuckNorrisApiService.getRandomJoke().value // Llama al servicio de la API para obtener el chiste
    }
}