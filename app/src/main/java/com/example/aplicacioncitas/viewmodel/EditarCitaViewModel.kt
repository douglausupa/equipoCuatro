package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarCitaViewModel @Inject constructor(
    private val repository: ICitaRepository
) : ViewModel() {

    fun actualizarCita(citaResponse: CitaResponse) = viewModelScope.launch {
        repository.actualizar(citaResponse)
    }
}
