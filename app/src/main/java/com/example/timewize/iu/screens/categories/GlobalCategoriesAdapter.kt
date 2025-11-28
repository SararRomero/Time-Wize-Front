package com.example.timewize.iu.screens.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timewize.R
import com.example.timewize.databinding.ItemGlobalCategoryBinding

class GlobalCategoriesAdapter(
    private val onEdit: (Category) -> Unit,
    private val onDelete: (Category) -> Unit
) : ListAdapter<Category, GlobalCategoriesAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    // Lista de colores predefinidos para las categorías
    private val categoryColors = listOf(
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4",
        "#FFD166", "#6A0572", "#118AB2", "#06D6A0"
    )

    inner class CategoryViewHolder(private val binding: ItemGlobalCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            binding.tvTaskCount.text = "${category.taskCount} tareas"

            // Usar emoji como icono simple
            binding.ivCategoryIcon.text = getCategoryEmoji(category.name)

            // Aplicar color a la categoría
            applyCategoryColor(category, binding)

            binding.btnMenu.setOnClickListener { view ->
                showCategoryMenu(category, view)
            }

            // Click en toda la tarjeta para editar
            binding.root.setOnClickListener {
                onEdit(category)
            }
        }

        private fun getCategoryEmoji(categoryName: String): String {
            return when {
                categoryName.contains("Personal", true) -> "👤"
                categoryName.contains("Estudio", true) -> "📚"
                categoryName.contains("Salud", true) -> "🏥"
                categoryName.contains("Ejercicio", true) -> "💪"
                categoryName.contains("Compras", true) -> "🛒"
                categoryName.contains("Trabajo", true) -> "💼"
                categoryName.contains("Proyectos", true) -> "📋"
                else -> "📁"
            }
        }

        private fun applyCategoryColor(category: Category, binding: ItemGlobalCategoryBinding) {
            try {
                val color = if (category.color.isNotEmpty()) {
                    Color.parseColor(category.color)
                } else {
                    val position = adapterPosition
                    val colorIndex = if (position >= 0) position % categoryColors.size else 0
                    Color.parseColor(categoryColors[colorIndex])
                }

                // Aplicar color al fondo del icono
                binding.ivCategoryIcon.setBackgroundColor(adjustAlpha(color, 0.2f))
                binding.ivCategoryIcon.setTextColor(color)

            } catch (e: Exception) {
                // Fallback a color por defecto si hay error
                val defaultColor = ContextCompat.getColor(binding.root.context, R.color.colorPrimary)
                binding.ivCategoryIcon.setBackgroundColor(adjustAlpha(defaultColor, 0.2f))
                binding.ivCategoryIcon.setTextColor(defaultColor)
            }
        }

        private fun adjustAlpha(color: Int, factor: Float): Int {
            val alpha = (Color.alpha(color) * factor).toInt()
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            return Color.argb(alpha, red, green, blue)
        }

        private fun showCategoryMenu(category: Category, view: View) {
            val popup = PopupMenu(view.context, view)
            popup.menu.add("Editar").setOnMenuItemClickListener {
                onEdit(category)
                true
            }
            popup.menu.add("Eliminar").setOnMenuItemClickListener {
                onDelete(category)
                true
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemGlobalCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Método para eliminar una categoría
    fun removeCategory(category: Category) {
        val currentList = currentList.toMutableList()
        currentList.removeAll { it.name == category.name }
        submitList(currentList)
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