package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.model.CitaResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class CitaRepository @Inject constructor() : ICitaRepository {
    private val db = FirebaseFirestore.getInstance()
    private val citasCollection = db.collection("citas")
    private val contadorDocRef = db.collection("contadores").document("citas")

    override suspend fun getNextId(): Long {
        val snapshot = contadorDocRef.get().await()
        return if (snapshot.exists()) {
            val currentId = snapshot.getLong("ultimoId") ?: 0L
            val nextId = currentId + 1
            contadorDocRef.update("ultimoId", nextId).await()
            nextId
        } else {
            contadorDocRef.set(mapOf("ultimoId" to 1)).await()
            1
        }
    }

    override suspend fun insertarCita(citaResponse: CitaResponse): Long {
        val nextId = getNextId()
        val nuevaCita = citaResponse.copy(id = nextId)
        citasCollection.document(nextId.toString()).set(nuevaCita).await()
        return nextId
    }

    override suspend fun actualizar(citaResponse: CitaResponse) {
        citasCollection.document(citaResponse.id.toString()).set(citaResponse).await()
    }

    override suspend fun obtenerPorId(id: Int): CitaResponse? {
        return citasCollection.document(id.toString()).get().await().toObject(CitaResponse::class.java)
    }

    override suspend fun obtenerTodasLasCitas(): List<CitaResponse> {
        return citasCollection.get().await().toObjects(CitaResponse::class.java)
    }

    override suspend fun eliminarCitaPorId(id: Int): Boolean {
        return try {
            citasCollection.document(id.toString()).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
