package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
=======
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.repository.CitaRepository
>>>>>>> 5772a7fc6b55230dd262119c5b38a21dfba32d8d
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarCitaViewModel @Inject constructor(
    private val repository: ICitaRepository
) : ViewModel() {

    fun actualizarCita(cita: Cita) = viewModelScope.launch {
        repository.actualizar(cita)
    }
}
