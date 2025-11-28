package com.example.timewize.iu.screens.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewize.R
import com.example.timewize.databinding.FragmentManageGlobalCategoriesBinding

class ManageGlobalCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentManageGlobalCategoriesBinding
    private lateinit var adapter: GlobalCategoriesAdapter

    private val categoriesList = mutableListOf(
        Category("Trabajo", R.drawable.ic_folder_category),
        Category("Estudio", R.drawable.ic_folder_category),
        Category("Salud", R.drawable.ic_folder_category),
        Category("Personal", R.drawable.ic_folder_category)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageGlobalCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el adaptador CORRECTAMENTE - sin pasar la lista en el constructor
        adapter = GlobalCategoriesAdapter(
            onEdit = { category ->
                // abrir modal para editar
                showEditCategoryDialog(category)
            },
            onDelete = { category ->
                // Eliminar categoría de forma más eficiente
                val position = categoriesList.indexOf(category)
                if (position != -1) {
                    categoriesList.removeAt(position)
                    adapter.notifyItemRemoved(position) // Más eficiente que notifyDataSetChanged
                }
            }
        )

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter

        // Pasar la lista al adaptador usando submitList
        adapter.submitList(categoriesList.toList())

        binding.fabAddCategory.setOnClickListener {
            // abrir modal para agregar
            showAddCategoryDialog()
        }
    }

    private fun showEditCategoryDialog(category: Category) {
        // Implementar diálogo de edición
        // Por ahora solo un Toast
        android.widget.Toast.makeText(requireContext(), "Editar: ${category.name}", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showAddCategoryDialog() {
        // Implementar diálogo para agregar categoría
        android.widget.Toast.makeText(requireContext(), "Agregar nueva categoría", android.widget.Toast.LENGTH_SHORT).show()
    }
}