package com.example.aplicacioncitas.webservice

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import com.example.aplicacioncitas.model.ImagenRazaResponse

interface DogApiService {
    @GET("breed/{raza}/images/random")
    fun obtenerImagenPorRaza(@Path("raza") raza: String): Call<ImagenRazaResponse>
}