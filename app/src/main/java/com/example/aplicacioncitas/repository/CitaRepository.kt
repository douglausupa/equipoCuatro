package com.example.aplicacioncitas.repository


import androidx.lifecycle.LiveData
import com.example.aplicacioncitas.data.CitaDao
import com.example.aplicacioncitas.model.Cita
import kotlinx.coroutines.flow.Flow

class CitaRepository(private val citaDao: CitaDao) {

    suspend fun insertar(cita: Cita) {
        citaDao.insertarCita(cita)
    }

    suspend fun actualizar(cita: Cita) {
        citaDao.actualizarCita(cita)
    }


    suspend fun obtenerPorId(id: Int): Cita? {
        return citaDao.obtenerCitaPorId(id)
    }

    suspend fun obtenerTodasLasCitas(): List<Cita> {
        return citaDao.obtenerTodasLasCitas()
    }
}
