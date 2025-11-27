package com.example.timewize.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timewize.R
import android.widget.TextView

class HomeAdmin : AppCompatActivity() {

    private lateinit var tvUsers: TextView
    private lateinit var tvActiveUsers: TextView
    private lateinit var tvCategorySummary: TextView
    private lateinit var tvProductivity: TextView
    private lateinit var tvBalance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        initViews()
        loadDummyData() // Esto luego irá contra tu backend
    }

    private fun initViews() {
        tvUsers = findViewById(R.id.tvUsers)
        tvActiveUsers = findViewById(R.id.tvActiveUsers)
        tvCategorySummary = findViewById(R.id.tvCategorySummary)
        tvProductivity = findViewById(R.id.tvProductivity)
        tvBalance = findViewById(R.id.tvBalance)
    }

    private fun loadDummyData() {
        // Más adelante estos valores vendrán del backend
        tvUsers.text = "1234"
        tvActiveUsers.text = "278"

        tvCategorySummary.text = "Trabajo 40%, Estudio 25%, Salud 15%, Ocio 20%"
        tvProductivity.text = "Índice global: 78/100"
        tvBalance.text = "Productivo 50% • Personal 30% • Descanso 20%"
    }
}
