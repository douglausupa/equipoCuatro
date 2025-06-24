package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.CitaRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class NuevaCitaViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val citarepository = CitaRepository()

    fun guardarCita(
        nombrePropietario: String,
        nombreMascota: String,
        raza: String,
        telefono: String,
        sintomas: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onError("Usuario no autenticado")
            return
        }

        val citaResponse = CitaResponse(
            userId = userId,
            nombrePropietario = nombrePropietario,
            nombreMascota = nombreMascota,
            raza = raza,
            telefono = telefono,
            sintomas = sintomas
        )

        viewModelScope.launch {
            try {
                citarepository.insertarCita(citaResponse)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al guardar: ${e.message}")
            }
        }
    }

}