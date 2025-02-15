package com.empresa.myapplication.Database

// Importamos las clases necesarias para el manejo de la base de datos y las operaciones de corutinas.
import com.empresa.myapplication.Database.Post
import com.empresa.myapplication.Database.PostDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// El repositorio actúa como intermediario entre la capa de datos y el resto de la aplicación.
// Es responsable de gestionar las operaciones CRUD de los posts en la base de datos.
class PostRepository @Inject constructor(
    private val postDao: PostDao // Inyectamos el PostDao que interactúa directamente con la base de datos.
) {
    // funcion para obtener todos los posts desde la base de datos.
    // Retorna un Flow que emite una lista de posts, permitiendo observación de los cambios en tiempo real.
    fun getAllPosts(): Flow<List<Post>> = postDao.obtenerTodosLosPosts()

    // funcion para insertar un nuevo post en la base de datos.
    // Usamos la palabra clave suspend para hacer que esta función pueda ejecutarse de manera asincrónica
    // dentro de una corutina (se ejecuta en segundo plano para no bloquear el hilo principal).
    suspend fun insertPost(post: Post) = postDao.insertarPost(post)

    // funcion para actualizar un post en la base de datos.
    // También suspendido para que pueda ejecutarse de manera asincrónica en una corutina.
    suspend fun updatePost(post: Post) = postDao.actualzarPost(post)

    // funcion para eliminar un post de la base de datos.
    // Igual que los métodos anteriores, es suspendido para ejecutarse de manera eficiente en segundo plano.
    suspend fun deletePost(post: Post) = postDao.eliminarPost(post)
}
