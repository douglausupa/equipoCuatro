package com.example.aplicacioncitas.data

import com.example.aplicacioncitas.model.CitaResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitaFirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private val citasCollection = db.collection("citas")

    // Insertar o actualizar una cita
    suspend fun insertarCita(cita: CitaResponse) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    // Actualizar una cita (similar a insertar, pero puedes separar lógica si necesitas)
    suspend fun actualizarCita(cita: CitaResponse) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    // Obtener una cita por ID
    suspend fun obtenerCitaPorId(id: Int): CitaResponse? {
        return citasCollection.document(id.toString()).get().await().toObject(CitaResponse::class.java)
    }

    // Obtener todas las citas (usando Flow/LiveData para UI)
    suspend fun obtenerTodasLasCitas(): List<CitaResponse> {
        return citasCollection.get().await().toObjects(CitaResponse::class.java)
    }

    suspend fun eliminarCitaPorId(id: Int) {
        citasCollection.document(id.toString()).delete().await()
    }
}