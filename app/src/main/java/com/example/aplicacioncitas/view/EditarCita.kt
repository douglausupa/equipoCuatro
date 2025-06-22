package com.example.aplicacioncitas.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.databinding.ActivityEditDateBinding
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.view.ui.home.HomeActivity
import com.example.aplicacioncitas.viewmodel.EditarCitaViewModel
import com.example.aplicacioncitas.viewmodel.RazasViewModel

class EditarCita : AppCompatActivity() {
    private lateinit var binding: ActivityEditDateBinding
    private lateinit var citaViewModel: EditarCitaViewModel
    private lateinit var razasViewModel: RazasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()


        //val dao = AppDatabase.getDatabase(this).citaDao()
        //val repository = CitaRepository(dao)
        //val viewModelFactory = CitaViewModelFactory(repository)
        //citaViewModel = ViewModelProvider(this, viewModelFactory)[EditarCitaViewModel::class.java]
        razasViewModel = ViewModelProvider(this)[RazasViewModel::class.java]


        val cita = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("cita", Cita::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("cita")
        }


        cita?.let {
            binding.etNombrePropietario.setText(it.nombrePropietario)
            binding.autoCompleteRaza.setText(it.raza)
            binding.etTelefono.setText(it.telefono)
            binding.etNombreMascota.setText(it.nombreMascota)
        }


        razasViewModel.fetchBreeds()
        razasViewModel.breedList.observe(this) { razas ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razas)
            binding.autoCompleteRaza.setAdapter(adapter)
            binding.autoCompleteRaza.threshold = 2
        }


        validarlimitecaracteres()
        setupTextWatchers()
        validarbotoneditar()
        volver()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btneditarcita.setOnClickListener {
            if (cita != null) {
                val citaActualizada = cita.copy(
                    nombrePropietario = binding.etNombrePropietario.text.toString(),
                    nombreMascota = binding.etNombreMascota.text.toString(),
                    raza = binding.autoCompleteRaza.text.toString(),
                    telefono = binding.etTelefono.text.toString(),
                )

                citaViewModel.actualizarCita(citaActualizada)

                Toast.makeText(this, "Cita actualizada correctamente", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "No se pudo editar la cita", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validarlimitecaracteres() {
        binding.etNombreMascota.filters = arrayOf(InputFilter.LengthFilter(15))
        binding.etNombrePropietario.filters = arrayOf(InputFilter.LengthFilter(30))
        binding.etTelefono.apply {
            filters = arrayOf(InputFilter.LengthFilter(10))
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private fun setupTextWatchers() {
        val campos = listOf(
            binding.etNombreMascota,
            binding.autoCompleteRaza,
            binding.etNombrePropietario,
            binding.etTelefono
        )

        campos.forEach { campo ->
            campo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    validarbotoneditar()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun validarbotoneditar() {
        val todosLlenos = listOf(
            binding.etNombreMascota.text?.isNotBlank() == true,
            binding.autoCompleteRaza.text?.isNotBlank() == true,
            binding.etNombrePropietario.text?.isNotBlank() == true,
            binding.etTelefono.text?.isNotBlank() == true
        ).all { it }

        binding.btneditarcita.isEnabled = todosLlenos
    }

    private fun volver(){
        binding.back.setOnClickListener {
            finish()
        }
    }
}
