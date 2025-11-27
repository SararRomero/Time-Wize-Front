package com.example.timewize.iu.screens

import com.example.timewize.R
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Animaciones
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // Views
        val logo = findViewById<ImageView>(R.id.app_icon)
        val fullName = findViewById<EditText>(R.id.et_fullname)
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val btnSignup = findViewById<Button>(R.id.btn_signup)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        // Animaciones iniciales
        logo.startAnimation(zoomIn)
        fullName.startAnimation(slideUp)
        email.startAnimation(slideUp)
        password.startAnimation(slideUp)

        // Navegar a pantalla de recuperación
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // TEMPORALMENTE: activar modo admin sin backend
        btnSignup.setOnClickListener { view ->
            view.startAnimation(clickScale)

            // Guardar rol admin
            saveUserRole("admin")

            // Navegar a Home (donde luego rediriges a pantallas admin)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // --- FUNCION PARA GUARDAR EL ROL (AGREGADA) ---
    private fun saveUserRole(role: String) {
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        prefs.edit().putString("role", role).apply()
    }
}
