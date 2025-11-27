package com.example.timewize.iu.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.timewize.R
import com.example.timewize.databinding.ActivityTaskFormBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private val calendar = Calendar.getInstance()
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa la vista con la fecha y hora actuales
        updateDateView()
        updateTimeView()

        // Asigna Listeners
        binding.tvDate.setOnClickListener { showDatePicker() }
        binding.tvTime.setOnClickListener { showTimePicker() }
        binding.tvCategory.setOnClickListener { showCategoryPicker() }
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener { saveTask() }
    }

    // --- Funciones de Fecha y Hora ---
    private fun updateDateView() {
        val dateFormat = SimpleDateFormat(
            "EEE, d 'de' MMM",
            Locale.Builder().setLanguage("es").setRegion("ES").build()
        )
        binding.tvDate.text = dateFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateView()
        }

        DatePickerDialog(
            this,
            R.style.TimeWizeDatePickerDialog,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateTimeView() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.tvTime.text = timeFormat.format(calendar.time)
    }

    private fun showTimePicker() {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeView()
        }

        TimePickerDialog(
            this,
            R.style.TimeWizeTimePickerDialog,
            timeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    // --- Función para Categoría ---
    private fun showCategoryPicker() {
        val categories = arrayOf("Trabajo", "Escuela", "Personal", "GYM", "Añadir categoría")
        val checkedItem = categories.indexOf(selectedCategory)

        AlertDialog.Builder(this)
            .setTitle("Seleccionar Categoría")
            .setSingleChoiceItems(categories, checkedItem) { dialog, which ->
                if (which == categories.size - 1) {
                    Toast.makeText(this, "Añadir nueva categoría...", Toast.LENGTH_SHORT).show()
                } else {
                    selectedCategory = categories[which]
                    binding.tvCategory.text = selectedCategory
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    // --- Guardar Tarea ---
    private fun saveTask() {
        val title = binding.etTitle.text.toString()
        val date = binding.tvDate.text.toString()
        val time = binding.tvTime.text.toString()
        val category = selectedCategory

        if (title.isBlank() || category.isNullOrBlank()) {
            Toast.makeText(this, "Por favor, completa Título y Categoría", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí iría la lógica de guardar en la base de datos (ROOM, Firebase, etc.)
        // Log.d("TaskFormActivity", "Tarea Guardada: Título=$title, Fecha=$date, Hora=$time, Categoría=$category")

        finish() // Cierra la Activity después de guardar
    }
}
