package com.example.timewize.iu.screens

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timewize.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para enviar recuperación
        binding.btnSendRecovery.setOnClickListener {
            val email = binding.etEmailRecovery.text.toString().trim()

            if (isValidEmail(email)) {
                // Llamar a tu backend después
                Toast.makeText(
                    this,
                    "Se ha enviado un link de recuperación a $email",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Ingresa un correo válido",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
