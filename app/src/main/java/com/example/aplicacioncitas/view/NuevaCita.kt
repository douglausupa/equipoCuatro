package com.example.aplicacioncitas.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.data.AppDatabase
import com.example.aplicacioncitas.databinding.NuevaCitaBinding
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.view.ui.home.HomeActivity
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacioncitas.viewmodel.RazasViewModel
import kotlinx.coroutines.launch
import com.example.aplicacioncitas.databinding.ActivityEditDateBinding

class NuevaCita : AppCompatActivity() {

    private lateinit var binding: NuevaCitaBinding
    private lateinit var citaRepository: CitaRepository
    private lateinit var razasViewModel: RazasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = NuevaCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val database = AppDatabase.getDatabase(applicationContext)
        citaRepository = CitaRepository(database.citaDao())


        razasViewModel = ViewModelProvider(this)[RazasViewModel::class.java]


        configurarDropdown()
        configurarInsets()
        configurarValidacion()
        cargarRazas()



        binding.btnGuardarCita.setOnClickListener {
            guardarCita()
        }

        binding.btnBack.setOnClickListener {
            navegarAHome()
        }
    }

    private fun cargarRazas() {
        razasViewModel.fetchBreeds()
        razasViewModel.breedList.observe(this) { razas ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razas)
            binding.autoCompleteRaza.setAdapter(adapter)
            binding.autoCompleteRaza.threshold = 2
        }
    }

    private fun navegarAHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun configurarDropdown() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.listaSintomas,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.dropdownSintomas.setAdapter(adapter)
        binding.dropdownSintomas.setOnClickListener {
            binding.dropdownSintomas.showDropDown()
        }
    }

    private fun configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configurarValidacion() {
        val campos = listOf(
            binding.etNombreMascota,
            binding.autoCompleteRaza,
            binding.etNombrePropietario,
            binding.etTelefono,
            binding.dropdownSintomas
        )

        campos.forEach { campo ->
            campo.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    validarCamposObligatorios()
                }
            })
        }
    }

    private fun guardarCita() {

        val sintomas = binding.dropdownSintomas.text.toString().trim()

        if (sintomas.isEmpty()) {
            Toast.makeText(this, "Selecciona un síntoma", Toast.LENGTH_SHORT).show()
            return
        }

        val cita = Cita(
            nombrePropietario = binding.etNombrePropietario.text.toString().trim(),
            nombreMascota = binding.etNombreMascota.text.toString().trim(),
            raza = binding.autoCompleteRaza.text.toString(),
            telefono = binding.etTelefono.text.toString().trim(),
            sintomas = sintomas
        )

        lifecycleScope.launch {
            try {
                citaRepository.insertar(cita)

                runOnUiThread {
                    Toast.makeText(this@NuevaCita, "Cita guardada!", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    val intent = Intent(this@NuevaCita, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@NuevaCita, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarCampos() {
        binding.etNombrePropietario.text?.clear()
        binding.etNombreMascota.text?.clear()
        binding.autoCompleteRaza.text?.clear()
        binding.etTelefono.text?.clear()
        binding.dropdownSintomas.text?.clear()
    }

    private fun validarCamposObligatorios() {
        val camposCompletos = binding.etNombreMascota.text?.isNotEmpty() == true &&
                binding.autoCompleteRaza.text?.isNotEmpty() == true &&
                binding.etNombrePropietario.text?.isNotEmpty() == true &&
                binding.etTelefono.text?.isNotEmpty() == true

        binding.btnGuardarCita.isEnabled = camposCompletos
    }
}