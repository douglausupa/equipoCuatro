package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.*
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleCitaViewModel @Inject constructor(
    private val citaRepository: ICitaRepository
) : ViewModel() {

    private val _citaEliminada = MutableLiveData<Boolean>()
    val citaEliminada: LiveData<Boolean> get() = _citaEliminada

    private val _cita = MutableLiveData<CitaResponse?>()
    val cita: LiveData<CitaResponse?> get() = _cita

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

    fun cargarCitaPorId(id: Int) {
        viewModelScope.launch {
            try {
                val citaEncontrada = citaRepository.obtenerPorId(id)
                _cita.value = citaEncontrada
            } catch (e: Exception) {
                _cita.value = null
            }
        }
    }
}



