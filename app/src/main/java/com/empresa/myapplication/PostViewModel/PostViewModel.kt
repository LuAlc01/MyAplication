package com.empresa.myapplication.PostViewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.myapplication.Cases.DeletePostUseCase
import com.empresa.myapplication.Cases.GetPostsUseCase
import com.empresa.myapplication.Cases.InsertPostUseCase
import com.empresa.myapplication.Cases.UpdatePostUseCase
import com.empresa.myapplication.Database.Post
import com.empresa.myapplication.Database.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postDao: PostDao,
    private val getPostsUseCase: GetPostsUseCase,
    private val insertPostUseCase: InsertPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    // StateFlow para almacenar la lista de posts
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // StateFlow para manejar errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Cargar los posts desde la base de datos
    fun loadPosts() {
        viewModelScope.launch {
            try {
                getPostsUseCase().collect { postList ->
                    _posts.value = postList
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar los posts: ${e.message}"
            }
        }
    }

    // Insertar un nuevo post
    fun insertPost(post: Post) {
        viewModelScope.launch {
            try {
                insertPostUseCase(post)
            } catch (e: Exception) {
                _error.value = "Error al insertar el post: ${e.message}"
            }
        }
    }

    // Actualizar un post existente
    fun updatePost(post: Post) {
        viewModelScope.launch {
            try {
                deletePostUseCase(post)
            } catch (e: Exception) {
                _error.value = "Error al eliminar el post: ${e.message}"
            }
        }
    }

    // Eliminar un post
    fun deletePost(post: Post) {
        viewModelScope.launch {
            try {
                deletePostUseCase(post)
            } catch (e: Exception) {
                _error.value = "Error al eliminar el post: ${e.message}"
            }
        }
    }
}

//Esto se encarga de interactuar con el DAO y actualizar el estado de la UI a través de StateFlow o LiveData.
//
//Hilt se encargará de inyectar la instancia del PostDao en el ViewModel automáticamente cuando sea necesario, como ya esta configurado en el módulo de Hilt (DatabaseModule). en POst.kt