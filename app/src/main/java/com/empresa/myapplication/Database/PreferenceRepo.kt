package com.empresa.myapplication.Database

// Importamos las clases necesarias para manejar SharedPreferences y la inyección de dependencias.
import android.content.Context
import javax.inject.Inject

// El PreferencesRepository maneja la interacción con SharedPreferences,
// permitiendo guardar y obtener valores de forma persistente.
class PreferencesRepository @Inject constructor(
    private val context: Context // Se inyecta el contexto de la aplicación, necesario para acceder a SharedPreferences.
) {
    // Creamos una instancia de SharedPreferences utilizando el contexto.
    // "my_app_prefs" es el nombre del archivo donde se guardarán los datos y Context.MODE_PRIVATE asegura que
    // los datos sean privados para esta aplicación.
    private val sharedPreferences = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

    // funcion para guardar un valor en SharedPreferences.
    // Usamos la funcion edit() para modificar SharedPreferences y luego putString() para almacenar un valor de tipo String.
    // la funcion apply() guarda los cambios de manera asíncrona (sin bloquear el hilo principal).
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply() // Guardamos el valor en SharedPreferences.
    }

    // funcion para obtener un valor de SharedPreferences.
    // Si la clave no existe, se devuelve un valor por defecto.
    fun getString(key: String, defaultValue: String): String {
        // getString() retorna el valor asociado a la clave proporcionada o el valor por defecto si la clave no existe.
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
