package com.example.timewize.iu.screens.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timewize.R
import com.example.timewize.databinding.ItemCategoryBinding

class GlobalCategoriesAdapter :
    ListAdapter<Category, GlobalCategoriesAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            binding.imgCategoryIcon.setImageResource(category.iconRes)

            // Click en editar
            binding.btnEdit.setOnClickListener {
                // Lógica para editar categoría
            }

            // Click en eliminar
            binding.btnDelete.setOnClickListener {
                // Lógica para eliminar categoría
            }

            // Click en toda la tarjeta
            binding.root.setOnClickListener {
                // Lógica para seleccionar categoría
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}