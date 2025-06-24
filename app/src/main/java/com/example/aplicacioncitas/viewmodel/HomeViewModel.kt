package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.*
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ICitaRepository
) : ViewModel() {

    private val _citas = MutableLiveData<List<CitaResponse>>()
    val citas: LiveData<List<CitaResponse>> get() = _citas

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun cargarCitas() {
        viewModelScope.launch {
            try {
                val lista = repository.obtenerTodasLasCitas()
                _citas.postValue(lista)
            } catch (e: Exception) {
                _error.postValue("Error al cargar citas: ${e.message}")
            }
        }
    }
}
