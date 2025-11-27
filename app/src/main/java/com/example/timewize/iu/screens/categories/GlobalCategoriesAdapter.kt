package com.example.timewize.iu.screens.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timewize.databinding.ItemCategoryBinding
import com.example.timewize.iu.screens.categories.Category

class GlobalCategoriesAdapter(
    private val categories: List<Category>,
    private val onEdit: (Category) -> Unit,
    private val onDelete: (Category) -> Unit
) : RecyclerView.Adapter<GlobalCategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.binding.imgCategoryIcon.setImageResource(category.icon)
        holder.binding.tvCategoryName.text = category.name

        holder.binding.btnEdit.setOnClickListener { onEdit(category) }
        holder.binding.btnDelete.setOnClickListener { onDelete(category) }
    }

    override fun getItemCount(): Int = categories.size
}
