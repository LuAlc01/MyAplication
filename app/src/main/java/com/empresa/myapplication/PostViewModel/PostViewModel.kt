package com.empresa.myapplication.PostViewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.myapplication.Database.Post
import com.empresa.myapplication.Database.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Anotación Hilt para inyectar dependencias en el ViewModel
@HiltViewModel
class PostViewModel @Inject constructor(
    private val postDao: PostDao
) : ViewModel() {

    // MutableStateFlow para manejar los datos de los posts
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    // Función para obtener todos los posts de la base de datos
    fun getAllPosts() {
        viewModelScope.launch {
            // Usamos el DAO para obtener los datos de la base de datos de manera asincrónica
            _posts.value = postDao.obtenerTodosLosPosts()
        }
    }

    // Función para insertar un nuevo post en la base de datos
    fun insertPost(post: Post) {
        viewModelScope.launch {
            postDao.insertarPost(post)
            // Después de insertar, actualizamos los posts
            getAllPosts()
        }
    }

    // Función para actualizar un post
    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDao.actualzarPost(post)
            getAllPosts()
        }
    }

    // Función para eliminar un post
    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDao.eliminarPost(post)
            getAllPosts()
        }
    }
}

//Esto se encarga de interactuar con el DAO y actualizar el estado de la UI a través de StateFlow o LiveData.
//
//Hilt se encargará de inyectar la instancia del PostDao en el ViewModel automáticamente cuando sea necesario, como ya esta configurado en el módulo de Hilt (DatabaseModule). en POst.kt