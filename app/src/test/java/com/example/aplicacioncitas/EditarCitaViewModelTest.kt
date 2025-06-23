package com.example.aplicacioncitas

import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.repository.ICitaRepository
import com.example.aplicacioncitas.viewmodel.EditarCitaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class EditarCitaViewModelTest {

    @Mock lateinit var mockRepository:ICitaRepository
    private lateinit var viewModel: EditarCitaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditarCitaViewModel(mockRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `actualizarCita deberia llamar al repositorio`() = runTest {
        // Given
        val citaTest = CitaResponse(
            id = 1,
            nombrePropietario = "Juan Pérez",
            nombreMascota = "Max",
            raza = "Labrador",
            telefono = "5551234567",
            sintomas = "Fiebre y falta de apetito"
        )

        // When
        viewModel.actualizarCita(citaTest)

        // Avanza el tiempo para que se complete la corrutina
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockRepository).actualizar(citaTest)
    }
}