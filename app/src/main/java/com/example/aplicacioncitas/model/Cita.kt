package com.example.aplicacioncitas.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlinx.parcelize.Parcelize


@Entity(tableName = "citas")
@Parcelize
data class CitaResponse(
    var id: Long = 0,
    val userId: String = "",
    val nombrePropietario: String = "",
    val nombreMascota: String = "",
    val raza: String = "",
    val telefono: String = "",
    val sintomas: String = "",
    @ServerTimestamp val fecha: Date? = null
) : Parcelable



