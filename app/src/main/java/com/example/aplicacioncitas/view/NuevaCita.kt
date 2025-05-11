package com.example.aplicacioncitas.view

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
import kotlinx.coroutines.launch

class NuevaCita : AppCompatActivity() {

    private lateinit var binding: NuevaCitaBinding
    private lateinit var citaRepository: CitaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = NuevaCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Room
        val database = AppDatabase.getDatabase(applicationContext)
        citaRepository = CitaRepository(database.citaDao())

        // Configurar elementos UI
        configurarDropdown()
        configurarInsets()
        configurarValidacion()

        // Click del botón Guardar
        binding.btnGuardarCita.setOnClickListener {
            guardarCita()
        }
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
            binding.etRaza,
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
        val cita = Cita(
            nombrePropietario = binding.etNombrePropietario.text.toString().trim(),
            nombreMascota = binding.etNombreMascota.text.toString().trim(),
            raza = binding.etRaza.text.toString().trim(),
            telefono = binding.etTelefono.text.toString().trim(),
            sintomas = binding.dropdownSintomas.text.toString().trim()
        )

        lifecycleScope.launch {
            try {
                citaRepository.insertar(cita)

                // Verificación inmediata
                val citasGuardadas = citaRepository.obtenerTodasLasCitas()
                Log.d("DB_DEBUG", "Citas en DB: ${citasGuardadas.size}")

                runOnUiThread {
                    Toast.makeText(this@NuevaCita, "Cita guardada!", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
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
        binding.etRaza.text?.clear()
        binding.etTelefono.text?.clear()
        binding.dropdownSintomas.text?.clear()
    }

    private fun validarCamposObligatorios() {
        val camposCompletos = binding.etNombreMascota.text?.isNotEmpty() == true &&
                binding.etRaza.text?.isNotEmpty() == true &&
                binding.etNombrePropietario.text?.isNotEmpty() == true &&
                binding.etTelefono.text?.isNotEmpty() == true &&
                binding.dropdownSintomas.text?.isNotEmpty() == true

        binding.btnGuardarCita.isEnabled = camposCompletos
    }
}