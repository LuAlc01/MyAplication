package com.empresa.myapplication.Cases

import com.empresa.myapplication.Database.PreferencesRepository
import javax.inject.Inject

/**
 * Caso de uso para guardar un valor de tipo `String` en las preferencias compartidas (SharedPreferences).
 * Este caso de uso se encarga de interactuar con el repositorio (`PreferencesRepository`)
 * para almacenar un valor asociado a una clave específica.
 *
 * @property preferencesRepository El repositorio que proporciona acceso a las preferencias compartidas.
 */
class SaveStringUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para guardar un valor de tipo `String` en las preferencias compartidas.
     *
     * @param key La clave asociada al valor que se va a guardar.
     * @param value El valor de tipo `String` que se va a guardar.
     */
    operator fun invoke(key: String, value: String) {
        preferencesRepository.saveString(key, value) // Llama al repositorio para guardar el valor
    }
}

/**
 * Caso de uso para obtener un valor de tipo `String` desde las preferencias compartidas (SharedPreferences).
 * Este caso de uso se encarga de interactuar con el repositorio (`PreferencesRepository`)
 * para recuperar un valor asociado a una clave específica.
 *
 * @property preferencesRepository El repositorio que proporciona acceso a las preferencias compartidas.
 */
class GetStringUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para obtener un valor de tipo `String` desde las preferencias compartidas.
     *
     * @param key La clave asociada al valor que se va a recuperar.
     * @param defaultValue El valor predeterminado que se devolverá si la clave no existe.
     * @return El valor de tipo `String` asociado a la clave, o el valor predeterminado si la clave no existe.
     */
    operator fun invoke(key: String, defaultValue: String): String {
        return preferencesRepository.getString(key, defaultValue) // Llama al repositorio para obtener el valor
    }
}