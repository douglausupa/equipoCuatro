package com.example.aplicacioncitas.webservice

import com.example.aplicacioncitas.model.RazasResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceRazas {

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<RazasResponse>
}