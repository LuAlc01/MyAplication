package com.empresa.myapplication.Cases

import com.empresa.myapplication.Database.ChuckNorrisRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener un chiste aleatorio de Chuck Norris.
 * Este caso de uso se encarga de interactuar con el repositorio (`ChuckNorrisRepository`)
 * para obtener un chiste aleatorio desde una API externa.
 *
 * @property chuckNorrisRepository El repositorio que proporciona acceso a los datos de Chuck Norris.
 */
class GetRandomJokeUseCase @Inject constructor(
    private val chuckNorrisRepository: ChuckNorrisRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para obtener un chiste aleatorio.
     *
     * @return Un `String` que contiene el chiste aleatorio.
     */
    suspend operator fun invoke(): String {
        return chuckNorrisRepository.getRandomJoke() // Llama al repositorio para obtener el chiste
    }
}