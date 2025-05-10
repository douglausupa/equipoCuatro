package com.example.aplicacioncitas.data

import androidx.room.*
import com.example.aplicacioncitas.model.Cita

@Dao
interface CitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCita(cita: Cita)

    @Update
    suspend fun actualizarCita(cita: Cita)

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtenerCitaPorId(id: Int): Cita?

    @Query("SELECT * FROM citas")
    suspend fun obtenerTodasLasCitas(): List<Cita>
}
