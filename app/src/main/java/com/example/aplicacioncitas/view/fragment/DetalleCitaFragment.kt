package com.example.aplicacioncitas.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.databinding.FragmentDetalleCitaBinding
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.model.ImagenRazaResponse
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.view.DetalleCitaActivity
import com.example.aplicacioncitas.view.EditarCita
import com.example.aplicacioncitas.view.ui.home.HomeActivity
import com.example.aplicacioncitas.viewmodel.DetalleCitaViewModel
import com.example.aplicacioncitas.viewmodel.DetalleCitaViewModelFactory
import com.example.aplicacioncitas.webservice.DogApiService
import com.example.aplicacioncitas.webservice.RetrofitRazas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetalleCitaFragment : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: DetalleCitaViewModel

    private var nombrePropietario: String? = null
    private var nombreMascota: String? = null
    private var raza: String? = null
    private var telefono: String? = null
    private var sintomas: String? = null
    private var id: String? = null

    private lateinit var dogApiService: DogApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nombrePropietario = it.getString("nombrePropietario")
            nombreMascota = it.getString("nombreMascota")
            raza = it.getString("raza")
            telefono = it.getString("telefono")
            sintomas = it.getString("sintomas")
            id = it.getString("id")
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)

        //val citaDao = AppDatabase.getDatabase(requireContext()).citaDao()
        //val repository = CitaRepository(citaDao)

        //val factory = DetalleCitaViewModelFactory(repository)
        //viewModel = ViewModelProvider(this, factory).get(DetalleCitaViewModel::class.java)

        dogApiService = RetrofitRazas.instance.create(DogApiService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Establecer los valores de los campos de texto
        binding.etPropietario.setText(nombrePropietario)
        binding.tituloNombreMascota.setText(nombreMascota)
        binding.etRaza.setText(raza)
        binding.etTelefono.setText(telefono)
        binding.etSintomas.setText(sintomas)
        id?.let {
            binding.tvTurno.text = it // Mostrar el ID en el TextView

        }

        eliminar()
        editarcita()
        debolver()
        cargarImagenRaza()

        // Observa si la cita fue eliminada
        viewModel.citaEliminada.observe(viewLifecycleOwner, Observer { eliminado ->
            if (eliminado) {
                Toast.makeText(requireContext(), "Cita eliminada con éxito", Toast.LENGTH_SHORT).show()
                // Ir a HomeActivity
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Cierra la actividad actual
            } else {
                Toast.makeText(requireContext(), "Hubo un error al eliminar la cita", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminar() {
        binding.fabEliminar.setOnClickListener {
            val idText = binding.tvTurno.text.toString()
            val idInt = idText.toIntOrNull()

            if (idInt != null) {
                // Llama al método del ViewModel para eliminar la cita por ID
                viewModel.eliminarCitaPorId(idInt)
            } else {
                Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editarcita() {
        binding.btnEditar.setOnClickListener {
            val cita = Cita(
                nombrePropietario = binding.etPropietario.text.toString(),
                nombreMascota = binding.tituloNombreMascota.text.toString(),
                raza = binding.etRaza.text.toString(),
                telefono = binding.etTelefono.text.toString(),
                sintomas = binding.etSintomas.text.toString(),
                //id = binding.tvTurno.text.toString().toIntOrNull() ?: 0 // Asegúrate de que el ID es Int
            )

            val intent = Intent(requireContext(), EditarCita::class.java).apply {
                putExtra("cita", cita) // Envía el objeto completo
            }
            startActivity(intent)
        }
    }

    private fun cargarImagenRaza() {
        val razaFormateada = raza?.replace(" ", "-")?.lowercase() ?: return

        dogApiService.obtenerImagenPorRaza(razaFormateada).enqueue(object : Callback<ImagenRazaResponse> {
            override fun onResponse(
                call: Call<ImagenRazaResponse>,
                response: Response<ImagenRazaResponse> // <- Response de Retrofit
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { cuerpoRespuesta ->
                        Glide.with(requireContext())
                            .load(cuerpoRespuesta.message) // Usamos el campo del objeto
                            .into(binding.ivMascota)
                    }
                }
            }

            override fun onFailure(call: Call<ImagenRazaResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            nombrePropietario: String,
            nombreMascota: String,
            raza: String,
            id: String,
            telefono: String,
            sintomas: String
        ) = DetalleCitaFragment().apply {
            arguments = Bundle().apply {
                putString("nombrePropietario", nombrePropietario)
                putString("nombreMascota", nombreMascota)
                putString("raza", raza)
                putString("telefono", telefono)
                putString("sintomas", sintomas)
                putString("id", id)
            }
        }
    }

    private fun debolver(){

        binding.backButton.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

    }
}
