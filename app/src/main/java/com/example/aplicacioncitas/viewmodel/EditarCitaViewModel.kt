package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.repository.CitaRepository
import kotlinx.coroutines.launch

class EditarCitaViewModel (private val repository: CitaRepository) : ViewModel() {

    fun actualizarCita(cita: Cita) = viewModelScope.launch {
        repository.actualizar(cita)
    }
}