package com.example.aplicacioncitas.repository

import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.repository.ICitaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
        @Binds
        abstract fun bindCitaRepository(
            citaRepository: CitaRepository
        ): ICitaRepository
    }
