package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.model.CitaResponse

interface ICitaRepository {

    suspend fun getNextId(): Long
    suspend fun insertarCita(citaResponse: CitaResponse): Long
    suspend fun actualizar(citaResponse: CitaResponse)
    suspend fun obtenerPorId(id: Int): CitaResponse?
    suspend fun obtenerTodasLasCitas(): List<CitaResponse>
    suspend fun eliminarCitaPorId(id: Int): Boolean
}