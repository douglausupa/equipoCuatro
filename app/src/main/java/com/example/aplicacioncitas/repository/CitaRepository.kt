package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CitaRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val citasCollection = db.collection("citas")
    private val contadorDocRef = db.collection("contadores").document("citas")

    suspend fun getNextId(): Long {
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

    /*suspend fun insertarCita(citaResponse: CitaResponse): String {
        val documentRef = citasCollection.document()
        val nuevaCita = citaResponse.copy(id = documentRef.id)
        documentRef.set(nuevaCita).await()
        return documentRef.id
    }*/
    suspend fun insertarCita(cita: Cita): Long {
        val nextId = getNextId()
        val nuevaCita = cita.copy(id = nextId)
        citasCollection.document(nextId.toString()).set(nuevaCita).await()
        return nextId
    }

    suspend fun actualizar(cita: Cita) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    /*suspend fun obtenerPorId(id: Int): CitaResponse? {
        return citasCollection.document(id.toString()).get().await().toObject<CitaResponse>()
    }*/

    /*suspend fun obtenerTodasLasCitas(): List<CitaResponse> {
        return citasCollection.get().await().toObjects(CitaResponse::class.java)
    }*/

    suspend fun eliminarCitaPorId(id: Int): Boolean {
        return try {
            citasCollection.document(id.toString()).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
