package com.example.aplicacioncitas.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.aplicacioncitas.databinding.FragmentDetalleCitaBinding
import com.example.aplicacioncitas.model.CitaResponse
import com.example.aplicacioncitas.model.ImagenRazaResponse
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.view.EditarCitaActivity
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
    private lateinit var dogApiService: DogApiService
    private var idCita: Int? = null
    private var cita: CitaResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idCita = arguments?.getInt("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)

        val repository = CitaRepository()
        val factory = DetalleCitaViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DetalleCitaViewModel::class.java]

        dogApiService = RetrofitRazas.instance.create(DogApiService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idCita?.let { viewModel.cargarCitaPorId(it) }

        viewModel.cita.observe(viewLifecycleOwner) { citaResponse ->
            if (citaResponse != null) {
                cita = citaResponse
                mostrarDatos(citaResponse)
            } else {
                Toast.makeText(context, "Cita no encontrada", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener { volver() }
        binding.btnEditar.setOnClickListener { irAEditar() }
        binding.fabEliminar.setOnClickListener { eliminarCita() }

        viewModel.citaEliminada.observe(viewLifecycleOwner) { eliminado ->
            if (eliminado) {
                Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDatos(c: CitaResponse) {
        binding.tituloNombreMascota.text = c.nombreMascota
        binding.etRaza.setText(c.raza)
        binding.etSintomas.setText(c.sintomas)
        binding.etPropietario.setText(c.nombrePropietario)
        binding.etTelefono.setText(c.telefono)
        binding.tvTurno.text = c.id.toString()
        cargarImagenRaza(c.raza)
    }

    private fun eliminarCita() {
        val idLong = cita?.id
        if (idLong != null) {
            viewModel.eliminarCitaPorId(idLong.toInt())
        } else {
            Toast.makeText(requireContext(), "ID inválido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irAEditar() {
        cita?.let {
            val intent = Intent(requireContext(), EditarCitaActivity::class.java)
            intent.putExtra("cita", it)
            startActivity(intent)
        }
    }

    private fun volver() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun cargarImagenRaza(nombreRaza: String) {
        val razaFormateada = nombreRaza.replace(" ", "-").lowercase()
        dogApiService.obtenerImagenPorRaza(razaFormateada).enqueue(object : Callback<ImagenRazaResponse> {
            override fun onResponse(call: Call<ImagenRazaResponse>, response: Response<ImagenRazaResponse>) {
                if (response.isSuccessful) {
                    Glide.with(requireContext())
                        .load(response.body()?.message)
                        .into(binding.ivMascota)
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
        fun newInstance(id: Int) = DetalleCitaFragment().apply {
            arguments = Bundle().apply {
                putInt("id", id)
            }
        }
    }
}
