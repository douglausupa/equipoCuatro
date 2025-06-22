package com.example.aplicacioncitas.data

import com.example.aplicacioncitas.model.Cita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CitaFirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private val citasCollection = db.collection("citas")


    suspend fun insertarCita(cita: Cita) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }

    suspend fun actualizarCita(cita: Cita) {
        citasCollection.document(cita.id.toString()).set(cita).await()
    }


    suspend fun obtenerCitaPorId(id: Int): Cita? {
        return citasCollection.document(id.toString()).get().await().toObject(Cita::class.java)
    }


    suspend fun obtenerTodasLasCitas(): List<Cita> {
        return citasCollection.get().await().toObjects(Cita::class.java)
    }

    suspend fun eliminarCitaPorId(id: Int) {
        citasCollection.document(id.toString()).delete().await()
    }
}