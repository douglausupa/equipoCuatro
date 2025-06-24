package com.example.aplicacioncitas.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "citas")
@Parcelize
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: String = "",
    val nombrePropietario: String,
    val nombreMascota: String,
    val raza: String,
    val telefono: String,
    val sintomas: String? = null
) : Parcelable