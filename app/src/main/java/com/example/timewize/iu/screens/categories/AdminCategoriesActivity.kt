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

    private var _binding: FragmentManageGlobalCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesAdapter: GlobalCategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageGlobalCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        categoriesAdapter = GlobalCategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoriesAdapter
        }

        // Datos de ejemplo
        val sampleCategories = listOf(
            Category("Trabajo", R.drawable.ic_folder_category),
            Category("Personal", R.drawable.ic_folder_category),
            Category("Estudio", R.drawable.ic_folder_category),
            Category("Salud", R.drawable.ic_folder_category)
        )
        categoriesAdapter.submitList(sampleCategories)
    }

    private fun setupClickListeners() {
        binding.fabAddCategory.setOnClickListener {
            // Aquí irá la lógica para agregar nueva categoría
            showAddCategoryDialog()
        }
    }

    private fun showAddCategoryDialog() {
        // Implementar diálogo para agregar categoría
        // Por ahora solo un mensaje
        android.widget.Toast.makeText(requireContext(), "Agregar categoría", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}