package com.example.aplicacioncitas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.view.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val animHuella = findViewById<LottieAnimationView>(R.id.animFingerprint)

        animHuella.setOnClickListener {
            mostrarDialogoBiometrico()
        }
    }

    private fun mostrarDialogoBiometrico() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Autenticación con Biometría")
        builder.setMessage("Ingrese su huella digital")
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setOnDismissListener {
            iniciarBiometria()
        }
        builder.show()
    }

    private fun iniciarBiometria() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Acceso concedido", Toast.LENGTH_SHORT).show()


                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login con huella")
            .setSubtitle("Toque el lector para autenticarse")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}