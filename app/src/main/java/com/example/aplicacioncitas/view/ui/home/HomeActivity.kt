package com.example.aplicacioncitas.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.view.DetalleCitaActivity
import com.example.aplicacioncitas.view.NuevaCita
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerViewCitas: RecyclerView
    private lateinit var fabAddCita: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewCitas = findViewById(R.id.recyclerViewCitas)
        fabAddCita = findViewById(R.id.fabAddCita)

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
                putExtra("id", cita.id)
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
        val db = FirebaseFirestore.getInstance()
        db.collection("citas")
            .get()
            .addOnSuccessListener { result ->
                val listaCitas = result.map { doc ->
                    Cita(
                        id = doc.id,
                        nombrePropietario = doc.getString("nombrePropietario") ?: "",
                        nombreMascota = doc.getString("nombreMascota") ?: "",
                        raza = doc.getString("raza") ?: "",
                        telefono = doc.getString("telefono") ?: "",
                        sintomas = doc.getString("sintomas") ?: ""
                    )
                }
                homeAdapter.updateList(listaCitas)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error cargando citas", Toast.LENGTH_SHORT).show()
            }
    }
}
