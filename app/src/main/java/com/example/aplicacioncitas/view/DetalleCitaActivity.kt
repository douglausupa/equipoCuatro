package com.example.aplicacioncitas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.view.fragments.DetalleCitaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetalleCitaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cita)

        if (savedInstanceState == null) {
            val id = intent.getIntExtra("id", -1)

            if (id != -1) {
                val fragment = DetalleCitaFragment.newInstance(id)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            } else {

                finish()
            }
        }
    }
}


