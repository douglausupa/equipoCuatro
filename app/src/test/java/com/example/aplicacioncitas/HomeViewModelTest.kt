package com.example.aplicacioncitas.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import com.example.aplicacioncitas.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: HomeViewModel

    @Mock
    lateinit var mockRepository: ICitaRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(mockRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarCitas actualiza LiveData correctamente`() = runTest {
        // Datos simulados
        val citasMock = listOf(
            CitaResponse(id = 1, nombreMascota = "Firulais", sintomas = "No come"),
            CitaResponse(id = 2, nombreMascota = "Luna", sintomas = "Fiebre")
        )

        `when`(mockRepository.obtenerTodasLasCitas()).thenReturn(citasMock)

        // Ejecutar
        viewModel.cargarCitas()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verificar
        Assert.assertEquals(citasMock, viewModel.citas.getOrAwaitValue())
    }

    @Test
    fun `cargarCitas maneja excepcion y publica error`() = runTest {
        val errorMsg = "Error de red"
        `when`(mockRepository.obtenerTodasLasCitas()).thenThrow(RuntimeException(errorMsg))

        viewModel.cargarCitas()
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals("Error al cargar citas: $errorMsg", viewModel.error.getOrAwaitValue())
    }
}
