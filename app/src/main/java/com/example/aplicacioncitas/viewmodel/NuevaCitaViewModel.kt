package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.auth.IAuthProvider
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.repository.ICitaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NuevaCitaViewModel @Inject constructor(
    private val authProvider: IAuthProvider,
    private val citarepository: ICitaRepository
) : ViewModel() {

    fun guardarCita(
        nombrePropietario: String,
        nombreMascota: String,
        raza: String,
        telefono: String,
        sintomas: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = authProvider.currentUserId ?: run {
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