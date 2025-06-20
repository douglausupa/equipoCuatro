package com.example.aplicacioncitas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.view.fragments.DetalleCitaFragment

class DetalleCitaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cita)


        if (savedInstanceState == null) {
            val fragment = DetalleCitaFragment().apply {
                arguments = intent.extras
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
