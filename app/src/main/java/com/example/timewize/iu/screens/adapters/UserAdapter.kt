package com.example.timewize.iu.screens.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.timewize.R
import com.example.timewize.iu.screens.models.User

class UserAdapter(
    private val users: List<User>,
    private val onUserAction: (User, String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtUserName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtUserEmail)
        val txtRole: TextView = itemView.findViewById(R.id.txtUserRole)
        val txtStatus: TextView = itemView.findViewById(R.id.txtUserStatus)
        val txtAvatar: TextView = itemView.findViewById(R.id.txtAvatar)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditUser)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.txtName.text = user.name
        holder.txtEmail.text = user.email
        holder.txtRole.text = user.role
        holder.txtStatus.text = user.status

        // Configurar color del estado - CORREGIDO
        when (user.status) {
            "Activo" -> holder.txtStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark)
            )
            "Inactivo" -> holder.txtStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark)
            )
            else -> holder.txtStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)
            )
        }

        // Generar iniciales para el avatar
        val initials = user.name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .joinToString("")
            .take(2)
            .uppercase()

        holder.txtAvatar.text = initials

        holder.btnEdit.setOnClickListener {
            onUserAction(user, "edit")
        }

        holder.btnDelete.setOnClickListener {
            onUserAction(user, "delete")
        }
    }

    override fun getItemCount(): Int = users.size
}