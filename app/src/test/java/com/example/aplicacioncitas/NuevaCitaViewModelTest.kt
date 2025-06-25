package com.example.aplicacioncitas

import com.example.aplicacioncitas.auth.IAuthProvider
import com.example.aplicacioncitas.repository.ICitaRepository
import com.example.aplicacioncitas.viewmodel.NuevaCitaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NuevaCitaViewModelTest {

    @Mock lateinit var mockAuthProvider: IAuthProvider
    @Mock lateinit var mockRepository: ICitaRepository

    private lateinit var viewModel: NuevaCitaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = NuevaCitaViewModel(mockAuthProvider, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `guardarCita deberia llamar al repositorio cuando el usuario esta autenticado`() = runTest {
        // Arrange
        whenever(mockAuthProvider.currentUserId).thenReturn("uid123")
        whenever(mockRepository.insertarCita(any())).thenReturn(1L)

        var success = false
        var error: String? = null

        // Act
        viewModel.guardarCita(
            nombrePropietario = "Ana",
            nombreMascota = "Rex",
            raza = "Pug",
            telefono = "3213213210",
            sintomas = "Tos",
            onSuccess = { success = true },
            onError = { error = it }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assert(success)
        assert(error == null)
        verify(mockRepository).insertarCita(any())
    }
}
