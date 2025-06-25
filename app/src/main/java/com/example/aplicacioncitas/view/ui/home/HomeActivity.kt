package com.example.aplicacioncitas.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.repository.CitaRepository
import com.example.aplicacioncitas.view.NuevaCita
import com.example.aplicacioncitas.view.DetalleCitaActivity
import com.example.aplicacioncitas.viewmodel.HomeViewModel
import com.example.aplicacioncitas.viewmodel.HomeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerViewCitas: RecyclerView
    private lateinit var fabAddCita: FloatingActionButton
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ViewModel con Factory
        val repository = CitaRepository()
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        // UI Setup
        recyclerViewCitas = findViewById(R.id.recyclerViewCitas)
        fabAddCita = findViewById(R.id.fabAddCita)

        setupRecyclerView()
        setupFab()
        observarViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarCitas()
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(emptyList()) { cita ->
            val intent = Intent(this, DetalleCitaActivity::class.java).apply {
                putExtra("id", cita.id.toInt())  // <- asegúrese que 'cita.id' no es null
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

    private fun observarViewModel() {
        viewModel.citas.observe(this) { lista ->
            homeAdapter.updateList(lista)
        }

        viewModel.error.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}

