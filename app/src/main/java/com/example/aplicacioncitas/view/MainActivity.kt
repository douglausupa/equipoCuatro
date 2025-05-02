package com.example.aplicacioncitas.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.aplicacioncitas.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.nueva_cita)

        // Inicializar dropdown de síntomas
        val dropdown = findViewById<AutoCompleteTextView>(R.id.dropdownSintomas)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.listaSintomas,
            android.R.layout.simple_dropdown_item_1line
        )
        dropdown.setAdapter(adapter)
        dropdown.setOnClickListener {
            dropdown.showDropDown()
        }

        // Activar validación en campos
        activarValidacionEnCampos()

        // Ajustar padding por los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarCamposObligatorios() {
        val nombreMascota = findViewById<EditText>(R.id.etNombreMascota).text.toString().trim()
        val raza = findViewById<EditText>(R.id.etRaza).text.toString().trim()
        val nombrePropietario = findViewById<EditText>(R.id.etNombrePropietario).text.toString().trim()
        val telefono = findViewById<EditText>(R.id.etTelefono).text.toString().trim()
        val sintomas = findViewById<AutoCompleteTextView>(R.id.dropdownSintomas).text.toString().trim()
        val botonGuardar = findViewById<Button>(R.id.btnGuardarCita)

        val camposCompletos = nombreMascota.isNotEmpty() &&
                raza.isNotEmpty() &&
                nombrePropietario.isNotEmpty() &&
                telefono.isNotEmpty() &&
                sintomas.isNotEmpty()

        botonGuardar.isEnabled = camposCompletos
    }

    private fun activarValidacionEnCampos() {
        val campos = listOf(
            findViewById<EditText>(R.id.etNombreMascota),
            findViewById<EditText>(R.id.etRaza),
            findViewById<EditText>(R.id.etNombrePropietario),
            findViewById<EditText>(R.id.etTelefono),
            findViewById<AutoCompleteTextView>(R.id.dropdownSintomas)
        )

        campos.forEach { campo ->
            campo.addTextChangedListener {
                validarCamposObligatorios()
            }
        }
    }
}