package com.example.aplicacioncitas.view

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.databinding.ActivityEditDateBinding

class EditarCita : AppCompatActivity() {
    private lateinit var binding: ActivityEditDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar binding PRIMERO
        binding = ActivityEditDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // 2. Configurar icono DESPUÉS de inicializar binding
        val icon = ContextCompat.getDrawable(this, R.drawable.pencil)?.apply {
            setBounds(0, 0, 64, 64)
        }


        // 3. Configuraciones iniciales
        validarlimitecaracteres()
        setupTextWatchers()
        validarbotoneditar() // Validación inicial

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
}