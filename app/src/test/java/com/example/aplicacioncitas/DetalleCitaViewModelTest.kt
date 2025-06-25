package com.example.aplicacioncitas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.repository.ICitaRepository
import com.example.aplicacioncitas.viewmodel.DetalleCitaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class DetalleCitaViewModelTest {

    // ✅ Regla para ejecutar LiveData de forma síncrona durante las pruebas
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    // ✅ Mocks del repositorio y observadores de LiveData
    @Mock lateinit var mockRepository: ICitaRepository
    @Mock lateinit var observerCita: Observer<CitaResponse?>
    @Mock lateinit var observerEliminada: Observer<Boolean>

    private lateinit var viewModel: DetalleCitaViewModel

    @Before
    fun setup() {
        // ✅ Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this)

        // ✅ Reemplaza el Dispatcher principal por uno de prueba (necesario para pruebas de corrutinas)
        Dispatchers.setMain(testDispatcher)

        // ✅ Instancia el ViewModel con el repositorio simulado
        viewModel = DetalleCitaViewModel(mockRepository)

        // ✅ Observa los LiveData del ViewModel con mocks para poder verificar sus cambios
        viewModel.cita.observeForever(observerCita)
        viewModel.citaEliminada.observeForever(observerEliminada)
    }

    @After
    fun tearDown() {
        // ✅ Restaura el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    @Test
    fun `debe cargar cita correctamente desde el repositorio`() = runTest {
        // ✅ Verifica que al cargar una cita, el LiveData se actualice correctamente

        // ✅ Datos simulados que se devolverán desde el repositorio
        val citaMock = CitaResponse(
            id = 1,
            nombrePropietario = "Carlos",
            nombreMascota = "Rex",
            raza = "Pitbull",
            telefono = "3123456789",
            sintomas = "Dolor de cabeza"
        )

        // ✅ Simula la respuesta del método suspendido del repositorio
        `when`(mockRepository.obtenerPorId(1)).thenReturn(citaMock)

        // ✅ Llama al método del ViewModel que internamente lanza una corrutina
        viewModel.cargarCitaPorId(1)

        // ✅ Espera que la corrutina interna finalice
        advanceUntilIdle()

        // ✅ Verifica que el observer recibió el valor simulado
        verify(observerCita).onChanged(citaMock)

        // ✅ Verifica que el método del repositorio fue llamado
        verify(mockRepository).obtenerPorId(1)

        // ✅ Muestra por consola el valor observado (útil en consola durante la prueba)
        println("✅ Cita cargada desde repositorio: ${viewModel.cita.value}")
    }

    @Test
    fun `debe eliminar cita correctamente`() = runTest {
        // ✅ Verifica que al eliminar una cita, se actualice el LiveData correspondiente

        // ✅ Simula que el repositorio devuelve true al eliminar la cita
        `when`(mockRepository.eliminarCitaPorId(1)).thenReturn(true)

        // ✅ Llama al método del ViewModel que internamente lanza una corrutina
        viewModel.eliminarCitaPorId(1)

        // ✅ Espera que la corrutina interna finalice
        advanceUntilIdle()

        // ✅ Verifica que el LiveData observador fue notificado correctamente
        verify(observerEliminada).onChanged(true)

        // ✅ Verifica que se llamó al método del repositorio
        verify(mockRepository).eliminarCitaPorId(1)

        // ✅ Muestra por consola el valor observado (útil en consola durante la prueba)
        println("✅ Resultado de eliminación: ${viewModel.citaEliminada.value}")
    }
}
