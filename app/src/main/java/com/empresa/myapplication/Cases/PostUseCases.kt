package com.empresa.myapplication.Cases

import com.empresa.myapplication.Database.Post
import com.empresa.myapplication.Database.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener una lista de posts desde el repositorio.
 * Este caso de uso se encarga de interactuar con el repositorio (`PostRepository`)
 * para obtener todos los posts almacenados en la base de datos.
 *
 * @property postRepository El repositorio que proporciona acceso a los datos de los posts.
 */
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para obtener una lista de posts.
     *
     * @return Un `Flow<List<Post>>` que emite la lista de posts.
     */
    suspend operator fun invoke(): Flow<List<Post>> = postRepository.getAllPosts()
}

/**
 * Caso de uso para insertar un nuevo post en la base de datos.
 * Este caso de uso se encarga de interactuar con el repositorio (`PostRepository`)
 * para insertar un nuevo post.
 *
 * @property postRepository El repositorio que proporciona acceso a los datos de los posts.
 */
class InsertPostUseCase @Inject constructor(
    private val postRepository: PostRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para insertar un nuevo post.
     *
     * @param post El post que se va a insertar en la base de datos.
     */
    suspend operator fun invoke(post: Post) = postRepository.insertPost(post)
}

/**
 * Caso de uso para actualizar un post existente en la base de datos.
 * Este caso de uso se encarga de interactuar con el repositorio (`PostRepository`)
 * para actualizar un post existente.
 *
 * @property postRepository El repositorio que proporciona acceso a los datos de los posts.
 */
class UpdatePostUseCase @Inject constructor(
    private val postRepository: PostRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para actualizar un post existente.
     *
     * @param post El post que se va a actualizar en la base de datos.
     */
    suspend operator fun invoke(post: Post) = postRepository.updatePost(post)
}

/**
 * Caso de uso para eliminar un post de la base de datos.
 * Este caso de uso se encarga de interactuar con el repositorio (`PostRepository`)
 * para eliminar un post existente.
 *
 * @property postRepository El repositorio que proporciona acceso a los datos de los posts.
 */
class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository // Inyección de dependencias del repositorio
) {
    /**
     * Función que ejecuta el caso de uso.
     * Llama al repositorio para eliminar un post existente.
     *
     * @param post El post que se va a eliminar de la base de datos.
     */
    suspend operator fun invoke(post: Post) = postRepository.deletePost(post)
}