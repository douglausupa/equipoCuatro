package com.example.aplicacioncitas.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacioncitas.databinding.ActivitySplashBinding
import com.example.aplicacioncitas.view.ui.home.LoginActivity



class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Espera 5 segundos, luego inicia LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // ← Transición suave
            finish()
        }, 5000)
    }

    override fun onBackPressed() {
        // Evita retroceder desde el splash
        finishAffinity()
    }
}
