package com.example.aplicacioncitas.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.view.NuevaCita
import com.example.aplicacioncitas.view.DetalleCitaActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerViewCitas: RecyclerView
    private lateinit var fabAddCita: FloatingActionButton
    private lateinit var citaRepository: CitaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewCitas = findViewById(R.id.recyclerViewCitas)
        fabAddCita = findViewById(R.id.fabAddCita)

        val database = AppDatabase.getDatabase(applicationContext)
        citaRepository = CitaRepository(database.citaDao())

        setupRecyclerView()
        setupFab()
        cargarCitasDesdeBD()
    }

    override fun onResume() {
        super.onResume()
        cargarCitasDesdeBD()
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(emptyList()) { cita ->
            val intent = Intent(this, DetalleCitaActivity::class.java).apply {
                putExtra("nombrePropietario", cita.nombrePropietario)
                putExtra("nombreMascota", cita.nombreMascota)
                putExtra("raza", cita.raza)
                putExtra("telefono", cita.telefono)
                putExtra("sintomas", cita.sintomas)
                putExtra("id", cita.id.toString())
            }
            startActivity(intent)
        }
        recyclerViewCitas.layoutManager = LinearLayoutManager(this)
        recyclerViewCitas.adapter = homeAdapter
    }

    private fun setupFab() {
        fabAddCita.setOnClickListener {
            val intent = Intent(this, NuevaCita::class.java)
            startActivity(intent)
        }
    }

    private fun cargarCitasDesdeBD() {
        lifecycleScope.launch {
            val citas = citaRepository.obtenerTodasLasCitas()
            homeAdapter.updateList(citas)
        }
    }
}
