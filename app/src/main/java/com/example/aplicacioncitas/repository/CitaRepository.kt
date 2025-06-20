package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
// import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CitaRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val citasCollection = db.collection("citas")

    suspend fun insertar(cita: Cita) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    suspend fun actualizar(cita: Cita) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    // suspend fun obtenerPorId(id: Int): Cita? {
    //     return citasCollection.document(id.toString()).get().await().toObject<Cita>()
    // }

    // suspend fun obtenerTodasLasCitas(): List<Cita> {
    //     return citasCollection.get().await().toObjects(Cita::class.java)
    // }

    suspend fun eliminarCitaPorId(id: Int): Boolean {
        return try {
            citasCollection.document(id.toString()).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
