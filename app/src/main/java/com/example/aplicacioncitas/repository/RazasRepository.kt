package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.model.RazasResponse
import com.example.aplicacioncitas.webservice.ApiServiceRazas
import com.example.aplicacioncitas.webservice.RetrofitRazas
import retrofit2.Response
import javax.inject.Inject

class RazasRepository @Inject constructor() {

    private val api: ApiServiceRazas = RetrofitRazas.instance.create(ApiServiceRazas::class.java)

    suspend fun getAllBreeds(): Response<RazasResponse> {
        return api.getAllBreeds()
    }
}
