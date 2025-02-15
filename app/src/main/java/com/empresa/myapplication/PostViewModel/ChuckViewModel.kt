package com.empresa.myapplication.PostViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.myapplication.Cases.GetRandomJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChuckNorrisViewModel @Inject constructor(
    private val getRandomJokeUseCase: GetRandomJokeUseCase
) : ViewModel() {

    // StateFlow para almacenar el chiste actual
    private val _joke = MutableStateFlow<String?>(null)
    val joke: StateFlow<String?> = _joke.asStateFlow()

    // StateFlow para manejar errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Cargar un chiste aleatorio desde la API
    fun loadRandomJoke() {
        viewModelScope.launch {
            try {
                _joke.value = getRandomJokeUseCase()
            } catch (e: Exception) {
                _error.value = "Error al cargar el chiste: ${e.message}"
            }
        }
    }
}