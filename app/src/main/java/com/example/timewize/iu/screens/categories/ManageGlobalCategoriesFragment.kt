package com.example.timewize.iu.screens.categories

import com.example.timewize.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewize.databinding.FragmentManageGlobalCategoriesBinding
import com.example.timewize.iu.screens.categories.Category


class ManageGlobalCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentManageGlobalCategoriesBinding

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

        val adapter = GlobalCategoriesAdapter(
            categoriesList,
            onEdit = { /* abrir modal */ },
            onDelete = { category ->
                categoriesList.remove(category)
                binding.rvCategories.adapter?.notifyDataSetChanged()
            }
        )

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter

        binding.fabAddCategory.setOnClickListener {
            // abrir modal
        }
    }
}
