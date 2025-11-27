package com.example.timewize.iu.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timewize.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navbar si lo usas
        NavbarHelper.setupNavbar(this)

        // Ejemplo de cómo acceder a los campos
        val userName = binding.inputUserName.text.toString()
        val userEmail = binding.txtUserEmail.text.toString()
        val userAge = binding.txtUserAge.text.toString()

        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Aquí abrirías Editar Perfil", Toast.LENGTH_SHORT).show()
        }

        binding.btnChangePhoto.setOnClickListener {
            Toast.makeText(this, "Aquí cambiarías la foto de perfil", Toast.LENGTH_SHORT).show()
        }
    }
}
