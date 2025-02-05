package com.empresa.myapplication.Database


import android.content.Context
import androidx.room.Room

//// Objeto singleton para gestionar la instancia de la base de datos
//// El patrón Singleton garantiza que solo haya una única instancia de la base de datos en toda la aplicación.
//object PostDatabaseInstance {
//
//    // Se marca como @Volatile para asegurar que las actualizaciones de la instancia sean visibles entre hilos.
//    @Volatile
//    private var INSTANCE: PostDatabase? = null
//
//    // Función para obtener la instancia de la base de datos
//    // Si la instancia ya existe, la devuelve, sino, la crea de forma segura.
//    fun getDatabase(context: Context): PostDatabase {
//        // Si la instancia ya fue creada, se devuelve la instancia existente.
//        return INSTANCE ?: synchronized(this) { // Bloque sincronizado para asegurar acceso seguro entre hilos.
//            // Si la instancia no existe, se crea la base de datos utilizando Room.
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                PostDatabase::class.java, // Especificamos la clase de la base de datos.
//                "post_database" // Nombre del archivo de base de datos
//            ).build() // Creación de la instancia de la base de datos.
//
//            // Guardamos la instancia creada en la variable INSTANCE para evitar crearla de nuevo.
//            INSTANCE = instance
//
//            // Devolvemos la instancia de la base de datos.
//            instance
//        }
//    }
//}
