package com.example.aplicacioncitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacioncitas.repository.CitaRepository

class DetalleCitaViewModelFactory(
    private val repository: CitaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleCitaViewModel::class.java)) {
            return DetalleCitaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
