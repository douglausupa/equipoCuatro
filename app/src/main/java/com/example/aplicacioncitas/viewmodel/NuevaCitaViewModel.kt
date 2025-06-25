package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD
import com.example.aplicacioncitas.auth.IAuthProvider
import com.example.aplicacioncitas.model.CitaResponse
=======
import com.example.aplicacioncitas.model.Cita
>>>>>>> 5772a7fc6b55230dd262119c5b38a21dfba32d8d
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

        val cita = Cita(
            userId = userId,
            nombrePropietario = nombrePropietario,
            nombreMascota = nombreMascota,
            raza = raza,
            telefono = telefono,
            sintomas = sintomas
        )

        viewModelScope.launch {
            try {
                citarepository.insertarCita(cita)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al guardar: ${e.message}")
            }
        }
    }
}