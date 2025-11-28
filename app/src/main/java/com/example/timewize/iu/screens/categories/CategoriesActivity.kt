package com.example.timewize.iu.screens.categories

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewize.databinding.FragmentManageGlobalCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: FragmentManageGlobalCategoriesBinding
    private lateinit var categoriesAdapter: GlobalCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentManageGlobalCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        checkEmptyState()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish() // Usar finish() en lugar de onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        categoriesAdapter = GlobalCategoriesAdapter(
            onEdit = { category ->
                showEditCategoryDialog(category)
            },
            onDelete = { category ->
                showDeleteConfirmationDialog(category)
            }
        )

        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
            adapter = categoriesAdapter
        }

        // Datos de ejemplo corregidos
        val sampleCategories = listOf(
            Category("Personal", android.R.drawable.ic_menu_edit, "#FF6B6B"),
            Category("Estudio", android.R.drawable.ic_menu_edit, "#4ECDC4"),
            Category("Salud", android.R.drawable.ic_menu_edit, "#45B7D1"),
            Category("Ejercicio", android.R.drawable.ic_menu_edit, "#96CEB4"),
            Category("Compras", android.R.drawable.ic_menu_edit, "#FFD166"),
            Category("Trabajo", android.R.drawable.ic_menu_edit, "#6A0572"),
            Category("Proyectos", android.R.drawable.ic_menu_edit, "#118AB2")
        )
        categoriesAdapter.submitList(sampleCategories)
    }

    private fun setupClickListeners() {
        binding.fabAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun checkEmptyState() {
        val isEmpty = categoriesAdapter.itemCount == 0
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvCategories.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showAddCategoryDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Nueva Categoría")
            .setMessage("Función de agregar categoría en desarrollo")
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showEditCategoryDialog(category: Category) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Editar Categoría")
            .setMessage("Editando: ${category.name}")
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showDeleteConfirmationDialog(category: Category) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Eliminar Categoría")
            .setMessage("¿Eliminar '${category.name}'?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                categoriesAdapter.removeCategory(category)
                checkEmptyState()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}