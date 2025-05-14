package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.repository.CitaRepository
import kotlinx.coroutines.launch

class DetalleCitaViewModel(private val citaRepository: CitaRepository) : ViewModel() {

    private val _citaEliminada = MutableLiveData<Boolean>()
    val citaEliminada: LiveData<Boolean> get() = _citaEliminada

    fun eliminarCitaPorId(id: Int) {
        viewModelScope.launch {
            try {
                val result = citaRepository.eliminarCitaPorId(id)
                _citaEliminada.value = result
            } catch (e: Exception) {
                _citaEliminada.value = false
            }
        }
    }
}
