package com.example.aplicacioncitas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncitas.repository.RazasRepository
import kotlinx.coroutines.launch

class RazasViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RazasRepository()

    private val _breedList = MutableLiveData<List<String>>()
    val breedList: LiveData<List<String>> = _breedList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchBreeds() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getAllBreeds()
                if (response.isSuccessful) {
                    val breeds = response.body()?.message?.keys?.toList() ?: emptyList()
                    _breedList.value = breeds
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

}