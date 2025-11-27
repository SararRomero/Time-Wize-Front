package com.example.timewize.iu.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.timewize.R

class HomeActivity : AppCompatActivity() {

    private lateinit var btnTodo: Button
    private lateinit var btnHecho: Button
    private lateinit var btnProgreso: Button
    private lateinit var btnColecciones: Button
    private lateinit var tasksContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // -----------------------------
        // ⭐ DETECTAR ROL GUARDADO
        // -----------------------------
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val role = prefs.getString("role", "user") ?: "user"

        if (role == "admin") {
            // Aquí decides qué pantalla de admin probar
            val intent = Intent(this, AdminUsersActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        // -----------------------------

        NavbarHelper.setupNavbar(this)

        // Inicializamos vistas
        btnTodo = findViewById(R.id.btnTodo)
        btnHecho = findViewById(R.id.btnHecho)
        btnProgreso = findViewById(R.id.btnProgreso)
        btnColecciones = findViewById(R.id.btnColecciones)
        tasksContainer = findViewById(R.id.tasksContainer)

        // Configurar navegación del navbar
        setupNavigation()

        val botones = listOf(btnTodo, btnHecho, btnProgreso, btnColecciones)

        // Listener compartido para los botones
        val filterListener = View.OnClickListener { view ->
            botones.forEach { it.isSelected = false } // todos deseleccionados
            val selected = view as Button
            selected.isSelected = true // el tocado se pone azul

            botones.forEach { it.setTextColor(ContextCompat.getColor(this, R.color.black)) }
            selected.setTextColor(ContextCompat.getColor(this, android.R.color.white))

            val filtro = selected.text.toString()
            mostrarTareas(filtro)
        }

        // Asignar listeners y fondo selector a todos los botones
        botones.forEach {
            it.setOnClickListener(filterListener)
            it.setBackgroundResource(R.drawable.button_filter_selector)
        }

        // Seleccionar por defecto el primero ("Todo")
        btnTodo.isSelected = true
        btnTodo.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        mostrarTareas("Todo")
    }

    private fun setupNavigation() {
        val navProfile = findViewById<ImageView>(R.id.navProfile)
        navProfile?.setOnClickListener {
            try {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("HomeActivity", "Error completo:", e)
            }
        }

        val btnAddTask = findViewById<View>(R.id.navAdd)
        btnAddTask?.setOnClickListener {
            openTaskForm()
        }
    }

    private fun openTaskForm() {
        val intent = Intent(this, TaskFormActivity::class.java)
        startActivity(intent)
    }

    private fun mostrarTareas(filtro: String) {
        tasksContainer.removeAllViews()

        when (filtro) {
            "Todo" -> {
                agregarTarea("Grocery shopping app design", "Market Research", "Hecho")
                agregarTarea("Grocery shopping app design", "Competitive Analysis", "En progreso")
                agregarTarea("Uber Eats redesign challenge", "Create Low-fidelity Wireframe", "Empezar")
            }
            "Hecho" -> agregarTarea("Grocery shopping app design", "Market Research", "Hecho")
            "En progreso" -> agregarTarea("Grocery shopping app design", "Competitive Analysis", "En progreso")
            "Colecciones" -> agregarTarea("Uber Eats redesign challenge", "Create Low-fidelity Wireframe", "Empezar")
        }
    }

    private fun agregarTarea(titulo: String, subtitulo: String, estado: String) {
        val inflater = LayoutInflater.from(this)
        val card = inflater.inflate(R.layout.item_task_card, null)

        val txtTitulo = card.findViewById<TextView>(R.id.txtTitulo)
        val txtSubtitulo = card.findViewById<TextView>(R.id.txtSubtitulo)
        val txtEstado = card.findViewById<TextView>(R.id.txtEstado)

        txtTitulo.text = titulo
        txtSubtitulo.text = subtitulo
        txtEstado.text = estado

        tasksContainer.addView(card)
    }
}
